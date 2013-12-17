rankhospital <- function(state, outcome, num = "best") {
  if (num == "best") num <- 1

  df <- read.csv("outcome-of-care-measures.csv", colClasses = "character")  
  dfred <- subset(df, select=c(Hospital.Name, State,
                               Hospital.30.Day.Death..Mortality..Rates.from.Heart.Attack,
                               Hospital.30.Day.Death..Mortality..Rates.from.Heart.Failure,
                               Hospital.30.Day.Death..Mortality..Rates.from.Pneumonia))
  names(dfred)[3] <- "heart attack"
  dfred[, 3] <- as.numeric(dfred[, 3])
  names(dfred)[4] <- "heart failure"
  dfred[, 4] <- as.numeric(dfred[, 4])
  names(dfred)[5] <- "pneumonia"
  dfred[, 5] <- as.numeric(dfred[, 5])
  
  if (! outcome %in% names(dfred)[3:5]) stop("invalid outcome") 
  
  dfState <- dfred[c("Hospital.Name", "State", outcome)]
  names(dfState)[3] <- "target"
  names(dfState)
  summary(dfState)
  dfState <- dfState[dfState$State == state & !is.na(dfState$target), ]
  if (nrow(dfState) == 0) stop("invalid state")
  if (nrow(dfState) < num) return(NA)
  
  dfState[with(dfState, order(target, Hospital.Name)), ][num,1]
}