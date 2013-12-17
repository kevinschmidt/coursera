x <- read.csv("hw1_data.csv")
head(x, n=2L)
tail(x, n=2L)
x[47,]

## select Ozone
y <- x["Ozone"]
## get number of rows that are NA
summary(y)
## get mean of non-NA rows
mean(y[!is.na(y)])

## select Ozone above 31 and Temp above 90
y <- x[!is.na(x$Ozone) & x$Ozone > 31, ]
z <- y[!is.na(y$Temp) & y$Temp > 90, ]
mean(z[["Solar.R"]])

## mean of Temp when month is 6
mean(x[x$Month==6,][["Temp"]])

## max of Ozone when month is 5
max(x[x$Month==5 & !is.na(x$Ozone),][["Ozone"]])
