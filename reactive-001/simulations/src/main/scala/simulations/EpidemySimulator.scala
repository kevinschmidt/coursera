package simulations

import math.random

class EpidemySimulator extends Simulator {

  def randomBelow(i: Int) = (random * i).toInt

  protected[simulations] object SimConfig {
    val population: Int = 300
    val roomRows: Int = 8
    val roomColumns: Int = 8

    val prevalenceRate = 0.01
    val transmissibilityRate = 0.4
    val deathRate = 0.25
    
    val maxMoveDelay = 5
    val sickDelay = 6
    val deadDelay = 14
    val immuneDelay = 16
    val healthyDelay = 18
  }

  import SimConfig._

  val persons: List[Person] = (0 to population-1).toList.map { id =>
    val person = new Person(id)
    if (id < prevalenceRate*population) person.checkInfected
    person.prepareMove
    person
  }

  class Person (val id: Int) {
    var infected = false
    var sick = false
    var immune = false
    var dead = false

    // demonstrates random number generation
    var row: Int = randomBelow(roomRows)
    var col: Int = randomBelow(roomColumns)
    
    def isInRoom(r: Int, c: Int, person: Person) = {
      (person.row == r) && (person.col == c)
    }

    def checkInfected() {
      if (!infected) {
        infected = true
        afterDelay(sickDelay) { setSick }
        afterDelay(deadDelay) { checkDead }
        afterDelay(immuneDelay) { checkImmune }
        afterDelay(healthyDelay) { checkHealthy }
      }
    }
    
    def setSick() {
      sick = true
    }
    
    def checkDead() {
      if (random < deathRate) {
        dead = true
      }
    }
    
    def checkImmune() {
      if (!dead) {
        immune = true
        sick = false
      }
    }
    
    def checkHealthy() {
      if (!dead) {
        infected = false
        immune = false
      }
    }
    
    def prepareMove() {
      if (!dead) {
        afterDelay(randomBelow(maxMoveDelay)+1) { move }
      }
    }
    
    def move() {
      if (!dead) {
        val suitableRooms = getSuitableRooms()
        if (suitableRooms.size > 0) {
          val newRoom = suitableRooms(randomBelow(suitableRooms.size))
          row = newRoom._1
          col = newRoom._2
          
          checkInfectedForCurrentRoom()
        }
        prepareMove
      }
    }
    
    def getSuitableRooms() = {
      val result = for {
        v <- row-1 to row+1
        h <- col-1 to col+1
        if (v == row || h == col)
        if !(v == row && h == col)
        r = if (v == -1) roomRows-1 else if (v == roomRows) 0 else v
        c = if (h == -1) roomColumns-1 else if (h == roomColumns) 0 else h
      } yield persons.foldLeft(false) { (value, person) =>
        if (isInRoom(r, c, person) && person.sick) true else value
      } match {
        case true => None
        case false => Some(r, c)
      }
      result.flatten
    }
    
    def checkInfectedForCurrentRoom() {
      persons.foreach { person =>
        if (isInRoom(row, col, person) && person.infected == true) {
          if (random < transmissibilityRate) checkInfected
        }
      }
    }
  }
}
