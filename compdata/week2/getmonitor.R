getmonitor <- function(id, directory, summarize = FALSE) {
  filename <- sprintf("%s/%03s.csv", directory, id)
  data <- read.csv(filename)
  if (summarize) {
    print(summary(data))
  }
  data
}