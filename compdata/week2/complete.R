complete <- function(directory, id = 1:332) {
  source("getmonitor.R")
  
  data <- data.frame(id=id, nobs=rep(NA, length(id)))
  
  for(i in seq(1, length(id))) {
    iData <- getmonitor(id[i], directory)
    data[i, "nobs"] <- nrow(iData[complete.cases(iData),])
  }
  
  ## Return a data frame of the form:
  ## id nobs
  ## 1  117
  ## 2  1041
  ## ...
  ## where 'id' is the monitor ID number and 'nobs' is the
  ## number of complete cases
  data
}