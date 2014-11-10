r <- 0.915
r2 <- r^2

p <- 0.11
z <- 1.96
n <- 100
se <- z*sqrt((p*(1-p))/n)
cat_interval <- p + c(-se, se)
cat_interval*n

k=1
n=4
p=0.12
sum(dbinom(k:n, size=n, p=p))

mean <- 248.3-244.8
p <- 0.0066
z <- abs(qnorm(p/2))
se <- (mean/z)
z98 <- abs(qnorm(0.01)) # 99%
c(mean-(z98*se), mean+(z98*se))

pf(3.47, 2, 828, lower.tail = FALSE)
a <- 0.05
k <- 3
a_star <- a / (k*(k-1)/2)

p <- 0.2
pe <- 0.24
n <- 3226
se_ci <- sqrt(pe*(1-pe)/n)
se_ht <- sqrt(p*(1-p)/n)

n <- 252
k <- 8
sse <- 3819.99
sst <- 15079.02
r_adj <- 1 - (sse/sst * (n-1)/(n-k-1))