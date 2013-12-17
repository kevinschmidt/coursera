rankall <- function(outcome, num = "best") {
  df <- read.csv("ProgAssignment3-data/outcome-of-care-measures.csv", colClasses = "character")  
  dfred <- subset(df, select=c(Hospital.Name, State,
                               Hospital.30.Day.Death..Mortality..Rates.from.Heart.Attack,
                               Hospital.30.Day.Death..Mortality..Rates.from.Heart.Failure,
                               Hospital.30.Day.Death..Mortality..Rates.from.Pneumonia))
  names(dfred)[3] <- "heart attack"
  dfred[, 3] <- suppressWarnings(as.numeric(dfred[, 3]))
  names(dfred)[4] <- "heart failure"
  dfred[, 4] <- suppressWarnings(as.numeric(dfred[, 4]))
  names(dfred)[5] <- "pneumonia"
  dfred[, 5] <- suppressWarnings(as.numeric(dfred[, 5]))
  
  if (! outcome %in% names(dfred)[3:5]) stop("invalid outcome") 
  
  dfred <- dfred[c("Hospital.Name", "State", outcome)]
  names(dfred)[3] <- "target"
  
  states <- sort(unique(dfred$State))
  data <- data.frame(hospital=rep("<NA>", length(states)), state=states, stringsAsFactors=FALSE)
  for (state in states) {
    dfState <- dfred[dfred$State == state & !is.na(dfred$target), ]
    if (nrow(dfState) == 0) stop("invalid state")
    stateNum <- num
    if (num == "best") stateNum <- 1
    if (num == "worst") stateNum <- nrow(dfState)
    if (nrow(dfState) >= stateNum) {
      data[data$state == state, 1] <- dfState[with(dfState, order(target, Hospital.Name)), ][stateNum,1]
    }
  }
  data
}