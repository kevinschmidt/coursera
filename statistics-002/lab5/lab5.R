source("http://bit.ly/dasi_inference")
load(url("http://www.openintro.org/stat/data/atheism.RData"))

head(atheism)
summary(atheism$nationality)
us12 = subset(atheism, atheism$nationality == "United States" & atheism$year == "2012")
head(us12)
summary(us12$response)
prop.table(table(us12), 1)

inference(us12$response, est = "proportion", type = "ci", method = "theoretical", success = "atheist")
n <- 1002
p <- 0.0499
se <- sqrt( (p*(1-p))/n )
z95 <- abs(qnorm(0.025))
me <- z95 * se

n <- 1000
p <- seq(0, 1, 0.01)
me <- 2 * sqrt(p * (1 - p)/n)
plot(me ~ p)

spain = subset(atheism, atheism$nationality == "Spain")
usa = subset(atheism, atheism$nationality == "United States")

inference(spain$response, as.factor(spain$year), est = "proportion", type = "ht", method = "theoretical", success = "atheist", null = 0, alternative = "twosided")
inference(usa$response, as.factor(usa$year), est = "proportion", type = "ht", method = "theoretical", success = "atheist", null = 0, alternative = "twosided")

p <- 0.5
z95 <- abs(qnorm(0.025))
me <- 0.01
n <- (z95^2 * p * (1-p)) / me^2