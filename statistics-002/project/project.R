download.file(url = "http://bit.ly/dasi_project_template", destfile = "dasi_project_template.Rmd")

load(url("http://bit.ly/dasi_gss_data"))
names(gss)
summary(gss$coninc)
hist(gss$coninc)
summary(gss$marital)
barplot(summary(gss$marital))
plot(gss$marital, gss$coninc)

gss_subset <- subset(gss, select=c(gss$coninc, gss$marital))
head(gss_subset, n=30)

#using samples
gssredux <- subset(gss, gss$coninc !='NA' & gss$marital !='NA')
gssredux <- gssredux[c("year", "coninc", "marital")]

marital_merged <- as.character(gssredux$marital)
marital_merged[marital_merged %in% c('Widowed', 'Divorced', 'Separated', 'Never Married')] <- "Not Married"
marital_merged <- as.factor(marital_merged)
marital_merged <- droplevels(marital_merged)
gssredux$marital_merged <- marital_merged

married <- gssredux[gssredux$marital_merged == 'Married',]
notmarried <- gssredux[gssredux$marital_merged == 'Not Married',]

#using samples
married_samples <- split(married, married$year)
notmarried_samples <- split(notmarried, notmarried$year)
means_samples = rep(NA, 29)
for(i in 1:29) {
  married_mean <- mean(married_samples[[i]]$coninc)
  notmarried_mean <- mean(notmarried_samples[[i]]$coninc)
  means_samples[i] = married_mean - notmarried_mean
}
hist(means_samples)

#using bootstrap
means_diff = rep(NA, 100)
for(i in 1:100) {
  married_sample = sample(married$coninc, length(married$coninc), replace = TRUE)
  notmarried_sample = sample(notmarried$coninc, length(notmarried$coninc), replace = TRUE)
  means_diff[i] = mean(married_sample) - mean(notmarried_sample)
}
hist(means_diff, main="Histogram of Bootstrapped Difference of Means")