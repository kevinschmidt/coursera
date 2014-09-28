z <- 2.33 # 98% confidence
sd <- 300
me <- 40
targetN <- (z*sd/me)^2

z <- 1.96 # 95% confidence
sd <- 300
me <- 25
targetN <- (z*sd/me)^2

mu <- 32
mean <- 30.69
sd <- 4.31
n <- 36
se <- sd / sqrt(n)
z <- (mean - mu) / se
pnorm(z)*2
low90 <- mean - (1.65*sd/sqrt(n))
high90 <- mean + (1.65*sd/sqrt(n))

mu <- 130
mean <- 134
sd <- 17
n <- 35
se <- sd / sqrt(n)
z <- (mean - mu) / se
(1-pnorm(z))*2

mu <- 500
mean <- 415
sd <- 220
n <- 100
se <- sd / sqrt(n)
z <- (mean - mu) / se
low95 <- mean - (1.96*se)
high95 <- mean + (1.96*se)