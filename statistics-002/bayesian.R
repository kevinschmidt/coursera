#prior
p1 <- 0.1
p2 <- 0.2
P_p1 <- 0.5
P_p2 <- 0.5

n <- 20
k <- 4
P_data_p1 = sum(dbinom(k, n, p1))
P_data_p2 = sum(dbinom(k, n, p2))
P_p1_data = P_data_p1 * P_p1 / ((P_data_p1 * P_p1) + (P_data_p2 * P_p2))
P_p2_data = 1 - P_p1_data