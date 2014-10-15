# part1
mu <- 10
mean <- 9.51
sd <- 4.65
n <- 40
se <- sd / sqrt(n)
z <- (mean - mu) / se
pnorm(z)

calcium <- c(-5, -4, -3, -2, 1, 7, 10, 11, 17, 18)
summary(calcium)
placebo <- c(-11, 5, -3, -3, -1, -1, -1, 2, 3, 5, 12)
summary(placebo)
summary(c(calcium, placebo))

# part2
qnorm(0.10)
z <- 1.28 # 80% confidence
sd <- 18
me <- 4
targetN <- (z*sd/me)^2

k=80
n=100
p=0.5
sum(dbinom(k:n, size=n, p=p))

pnorm(260, mean = 150, sd = 50)
z1 <- (260 - 150) / 50
pnorm(260, mean = 150, sd = 100)
z2 <- (260 - 150) / 100

z1 <- 1.28 # 80% confidence
z2 <- 2.33 # 98% confidence
sd <- 18
me <- 4
targetN1 <- (z1*sd/me)^2
targetN2 <- (z2*sd/me)^2