load(url("http://www.openintro.org/stat/data/kobe.RData"))
head(kobe)
kobe$basket[1:9]

kobe_streak <- calc_streak(kobe$basket)
barplot(table(kobe_streak))
summary(kobe_streak)

outcomes <- c("heads", "tails")
sample(outcomes, size=1, replace=TRUE)
sim_fair_coin <- sample(outcomes, size=100, replace=TRUE)
table(sim_fair_coin)
sim_unfair_coin <- sample(outcomes, size=100, replace=TRUE, prob=c(0.2, 0.8))
table(sim_unfair_coin)

outcomes <- c("H", "M")
sim_basket <- sample(outcomes, size=133, replace=TRUE, prob=c(0.45, 0.55))
table(sim_basket)

rand_streak <- calc_streak(sim_basket)
barplot(table(rand_streak))
summary(rand_streak)

pnorm(24, 21, 5)
qnorm(0.9, 21, 5)
choose(9, 2)
dbinom(70, size=245, p=0.25)
sum(dbinom(70:245, size=245, p=0.25))
sum(dbinom(1:70, size=245, p=0.25))
pbinom(70, size=245, p=0.25)


x=20:100
y=dbinom(x, size=245, p=0.25)
plot(x, y, type="h", lwd=10, lend="square")

bindat <- rbinom(1000, size=50, p=0.8)
hist(bindat, breaks=seq(20,60,1))
lines(seq(20,60,1), dbinom(seq(20,60,1), size=50, p=0.8) * 1000, col="green")

dbinom(6, size=10, p=0.56)
dbinom(600, size=1000, p=0.56)
sum(dbinom(0:10, size=10, p=0.07))

k=50
n=160
p=0.28
sum(dbinom(k:n, size=n, p=p))
mu=n*p
s=sqrt(n*p*(1-p))
1-pnorm(k,mu,s)
qnorm(0.025)
qnorm(0.05)