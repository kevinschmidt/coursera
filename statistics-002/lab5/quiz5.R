# hypotheses test se calculation
n <- 50
p <- 0.3
pe <- 0.36
se <- sqrt(p*(1-p)/n)

fullstop <- c(6, 6)
rollingstop <- c(16, 15)
nostop <- c(4, 3)
total <- sum(fullstop) + sum(rollingstop) + sum(nostop)
males <- fullstop[2] + rollingstop[2] + nostop[2]
nostop_perc <- sum(nostop)/total
male_nostop_expected <- males * nostop_perc

np1 <- 71
np2 <- 224
n1 <- 144
n2 <- 389
p1 <- np1 / n1
p2 <- np2 / n2
p_pool <- (np1 + np2) / (n1 + n2)
se <- sqrt( ((p_pool*(1-p_pool))/n1) + ((p_pool*(1-p_pool))/n2) )

n <- 50
p <- 0.3
se <- sqrt( (p*(1-p))/n )

n <- 500
np1 <- (n/2)*0.35
np2 <- (n/2)*0.55
(np1 + np2) / n