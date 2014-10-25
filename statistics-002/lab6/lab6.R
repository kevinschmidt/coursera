load(url("http://www.openintro.org/stat/data/mlb11.RData"))
summary(mlb11)

plot(x=mlb11$at_bats, y=mlb11$runs)
cor(mlb11$at_bats, mlb11$runs)
plot_ss(x = mlb11$at_bats, y = mlb11$runs, showSquares = TRUE)
m1 <- lm(runs ~ at_bats, data = mlb11)
summary(m1)
m2 <- lm(runs ~ homeruns, data = mlb11)
summary(m2)

plot(mlb11$runs ~ mlb11$at_bats)
abline(m1)
mlb11$residuals <- m1$residuals
mlb11[mlb11$at_bats==5579,]

plot(m1$residuals ~ mlb11$at_bats)
abline(h = 0, lty = 3) # adds a horizontal dashed line at y = 0
hist(m1$residuals)
qqnorm(m1$residuals)
qqline(m1$residuals) # adds diagonal line to the normal prob plot

cor(mlb11$at_bats, mlb11$runs)
cor(mlb11$homeruns, mlb11$runs)
cor(mlb11$hits, mlb11$runs)
cor(mlb11$wins, mlb11$runs)
cor(mlb11$bat_avg, mlb11$runs)
cor(mlb11$new_obs, mlb11$runs)
cor(mlb11$new_slug, mlb11$runs)
cor(mlb11$new_onbase, mlb11$runs)