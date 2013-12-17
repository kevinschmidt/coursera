library(lattice)

## 1.
outcome <- read.csv("outcome-of-care-measures.csv", colClasses = "character")
head(outcome)
outcome[, 11] <- as.numeric(outcome[, 11])
hist(outcome[, 11])
hist(outcome[, 11], main="Heart Attack 30-day Death Rate", xlab="30-day Death Rate")

## 2.
outcome[, 17] <- as.numeric(outcome[, 17])
outcome[, 23] <- as.numeric(outcome[, 23])

par(mfrow = c(3, 1))
minValue <- min(outcome[,11], outcome[,17], outcome[,23], na.rm=T)
maxValue <- max(outcome[,11], outcome[,17], outcome[,23], na.rm=T)
hist(outcome[, 11], main=substitute(paste("Heart Attack (", bar(x) == k, ")"), list(k=mean(outcome[,11], na.rm=T))), xlab="30-day Death Rate", xlim=range(minValue, maxValue), prob=T)
abline(v=median(outcome[, 11], na.rm=T))
lines(density(outcome[, 11], na.rm=T))
hist(outcome[, 17], main=substitute(paste("Heart Failure (", bar(x) == k, ")"), list(k=mean(outcome[,17], na.rm=T))), xlab="30-day Death Rate", xlim=range(minValue, maxValue), prob=T)
abline(v=median(outcome[, 17], na.rm=T))
lines(density(outcome[, 17], na.rm=T))
hist(outcome[, 23], main=substitute(paste("Pneumonia (", bar(x) == k, ")"), list(k=mean(outcome[,23], na.rm=T))), xlab="30-day Death Rate", xlim=range(minValue, maxValue), prob=T)
abline(v=median(outcome[, 23], na.rm=T))
lines(density(outcome[, 23], na.rm=T))

## 3.
par(mfrow = c(1, 1), las=2)
table(outcome$State)
outcome2 <- subset(outcome, table(outcome$State)[outcome$State] > 20)
death <- outcome2[, 11]
state <- outcome2$State
boxplot(death ~ state)
boxplot(death ~ state, main="Heart Attack 30-day Death Rate by State", xlab="30-day Death Rate")
## reorder by median of state
outcome2$State <- with(outcome2, reorder(outcome2$State, outcome2[,11], median, na.rm=T))
death <- outcome2[, 11]
state <- outcome2$State
boxplot(death ~ state, main="Heart Attack 30-day Death Rate by State", xlab="30-day Death Rate")

## 4.
outcome <- read.csv("outcome-of-care-measures.csv", colClasses = "character")
hospital <- read.csv("hospital-data.csv", colClasses = "character")
outcome.hospital <- merge(outcome, hospital, by = "Provider.Number")

death <- as.numeric(outcome.hospital[, 11]) ## Heart attack outcome
npatient <- as.numeric(outcome.hospital[, 15])
owner <- factor(outcome.hospital$Hospital.Ownership)

xyplot(death ~ npatient | owner, main="Heart Attack 30-day Death Rate by Ownership",
       xlab="Number of Patients Seen", ylab="30-day Death Rate",
       panel = function(x, y, ...) {
         panel.xyplot(x, y, ...)
         panel.lmline(x, y)
       })
