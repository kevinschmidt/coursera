source("http://www.openintro.org/stat/data/present.R")
present
dim(present)
names(present)
present$boys
plot(x = present$year, y = present$girls, type = "l")
present$boys + present$girls
plot(x = present$year, y = present$boys + present$girls, type = "l")
which.max(present$boys + present$girls)
present$boys / (present$boys + present$girls)
present$boys > present$girls
plot(x = present$year, y = present$boys / (present$boys + present$girls), type = "l")
which.max(present$boys - present$girls)
