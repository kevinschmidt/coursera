agecount <- function(age = NULL) {
  ## Check that "cause" is non-NULL; else throw error
  if (is.null(age)) stop("no age")
  ## Read "homicides.txt" data file
  homicides <- readLines("homicides.txt")
  ## Extract ages of victims; ignore records where no age is given
  r <- regexec("(<br./>[A|a]ge:|<dd>.*?,) (.*?) years old</dd>", homicides)
  ages <- sapply(regmatches(homicides, r), function(x) as.numeric(x[3]))
  freq.ages <- as.data.frame(table(ages))
  ## Return integer containing count of homicides for that age
  result <- freq.ages[freq.ages$ages==age,][[2]]
  if (length(result) == 0) return(0)
  result
}