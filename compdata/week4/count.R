count <- function(cause = NULL) {
  ## Check that "cause" is non-NULL; else throw error
  if (is.null(cause)) stop("no cause")
  ## Check that specific "cause" is allowed; else throw error
  if (! cause %in% c("asphyxiation", "blunt force", "other", "shooting", "stabbing", "unknown")) stop("bad cause")
  ## Read "homicides.txt" data file
  homicides <- readLines("homicides.txt")
  ## Extract causes of death
  r <- regexec("<dd>[C|c]ause: (.*?)</dd>", homicides)
  causes <- sapply(regmatches(homicides, r), function(x) x[2])
  causes <- sapply(causes, function(x) tolower(x))
  freq.causes <- as.data.frame(table(causes))
  ## Return integer containing count of homicides for that cause
  freq.causes[freq.causes$causes==cause,][[2]]
}