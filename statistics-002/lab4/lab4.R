load(url("http://bit.ly/dasi_nc"))

summary(nc)
gained_clean = na.omit(nc$gained)
n = length(gained_clean)

boot_means = rep(NA, 100)
for(i in 1:100) {hist
  boot_sample = sample(gained_clean, n, replace = TRUE)
  boot_means[i] = mean(boot_sample)
}
summary(boot_means)
hist(boot_means)
quantile(boot_means, c(.05, .95))


source("http://bit.ly/dasi_inference")

inference(nc$gained, type = "ci", method = "simulation", conflevel = 0.9, est = "mean", boot_method = "perc")
inference(nc$gained, type = "ci", method = "simulation", conflevel = 0.95, est = "mean", boot_method = "perc")
inference(nc$gained, type = "ci", method = "simulation", conflevel = 0.95, est = "mean", boot_method = "se")
inference(nc$gained, type = "ci", method = "simulation", conflevel = 0.95, est = "median", boot_method = "se")

boxplot(nc$weight ~ nc$habit)
by(nc$weight, nc$habit, mean)
inference(y = nc$weight, x = nc$habit, est = "mean", type = "ht", null = 0, alternative = "twosided", method = "theoretical")
inference(y = nc$weight, x = nc$habit, est = "mean", type = "ci", conflevel = 0.95, method = "theoretical")
by(nc$mage, nc$mature, max)
by(nc$mage, nc$mature, min)

load(url("http://bit.ly/dasi_gss_ws_cl"))
summary(gss)
a <- 0.05
k <- 4
K <- k*(k-1)/2
a_bon <- a/K
inference(y = gss$wordsum, x = gss$class, est = "mean", type = "ht", alternative = "greater", method = "theoretical")