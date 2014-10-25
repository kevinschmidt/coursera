library(DAAG)
data(allbacks)
book_mlr = lm(weight ~ volume + cover, data = allbacks)
summary(book_mlr)

states = read.csv("http://bit.ly/dasi_states")
pov_slr = lm(poverty ~ female_house, data=states)
summary(pov_slr)
anova(pov_slr)
pov_mlr = lm(poverty ~ female_house + white, data=states)
summary(pov_mlr)
anova(pov_mlr)

cognitive = read.csv("http://bit.ly/dasi_cognitive")
summary(cognitive)
cog_full = lm(kid_score ~ mom_hs + mom_iq + mom_work + mom_age, data=cognitive)
summary(cog_full)
pt(2.201, df=429, lower.tail=FALSE) * 2
c(2.54 - abs(qt(0.025, df=429)) * 2.35, 2.54 + abs(qt(0.025, df=429)) * 2.35) # 95%
cog_final = lm(kid_score ~ mom_hs + mom_iq + mom_work, data=cognitive)
summary(cog_final)

plot(cog_final$residuals ~ cognitive$mom_iq)
hist(cog_final$residuals)
qqnorm(cog_final$residuals)
qqline(cog_final$residuals)
plot(cog_final$residuals ~ cog_final$fitted)
plot(abs(cog_final$residuals) ~ cog_final$fitted)
plot(cog_final$residuals)