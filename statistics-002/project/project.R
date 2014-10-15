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