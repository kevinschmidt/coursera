mean <- -3.48
se <- 1.3
z95 <- abs(qnorm(0.025)) # 95%
z90 <- abs(qnorm(0.05)) # 90%
low90 <- mean + (z90*se)
high90 <- mean - (z90*se)
low95 <- mean + (z95*se)
high95 <- mean - (z95*se)
z <- (abs(mean) - 0) / se
2*pnorm(-z)

t <- 2.485
n <- 26
df <- n-1
2*pt(t, df, lower.tail=FALSE)

t <- 0.5
n <- 28
df <- n-1
pt(t, df, lower.tail=FALSE)

MSG <- 75
MSE <- 40.13
f <- MSG/MSE

a <- 0.05
k <- 5
K <- k*(k-1)/2
a_bon <- a/K

mean <- -3.48
sd <- 13
n <- 200
se <- sd / sqrt(n)
z <- (mean - 0) / se
2*pnorm(z)

n <- 45
k <- 5
dft <- n-1
dfg <- k-1
dfe <- dft-dfg