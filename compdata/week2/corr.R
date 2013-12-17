corr <- function(directory, threshold = 0) {
  source("complete.R")
  
  nobs <- complete(directory)
  nobsThresh <- nobs[nobs$nobs > threshold,][[1]]
  
  result <- vector("numeric")
  for (i in nobsThresh) {
    iData <- getmonitor(i, directory)
    result <- append(result, cor(iData[[2]], iData[[3]], use="complete.obs"))
  }
  result
}