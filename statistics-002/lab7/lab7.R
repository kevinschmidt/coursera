load(url("http://www.openintro.org/stat/data/evals.RData"))

summary(evals)
hist(evals$score)
length(evals[evals$score < 3,]$score)

plot(evals$score ~ evals$bty_avg)
plot(jitter(evals$score) ~ jitter(evals$bty_avg))
m_bty = lm(score ~ bty_avg, data=evals)
summary(m_bty)
abline(m_bty)

hist(m_bty$residuals)
qqnorm(m_bty$residuals)
qqline(m_bty$residuals)
plot(m_bty$residuals ~ evals$bty_avg)
plot(m_bty$residuals ~ m_bty$fitted)
plot(m_bty$residuals)

plot(evals$bty_avg ~ evals$bty_f1lower)
cor(evals$bty_avg, evals$bty_f1lower)
plot(evals[,13:19])

m_bty_gen <- lm(score ~ bty_avg + gender, data = evals)
summary(m_bty_gen)
multiLines(m_bty_gen)
m_bty_rank <- lm(score ~ bty_avg + rank, data = evals)
summary(m_bty_rank)
multiLines(m_bty_rank)
m_full <- lm(score ~ rank + ethnicity + gender + language + age + cls_perc_eval
             + cls_students + cls_level + cls_profs + cls_credits + bty_avg, data = evals)
summary(m_full)

m_final1 <- lm(score ~ rank + ethnicity + gender + language + age + cls_perc_eval
             + cls_students + cls_level + cls_profs + cls_credits, data = evals)
summary(m_final1)
m_final2 <- lm(score ~ rank + ethnicity + gender + language + age + cls_perc_eval
             + cls_students + cls_level + cls_credits + bty_avg, data = evals)
summary(m_final2)
m_final3 <- lm(score ~ rank + ethnicity + gender + language + age + cls_perc_eval
             + cls_level + cls_profs + cls_credits + bty_avg, data = evals)
summary(m_final3)
m_final4 <- lm(score ~ ethnicity + gender + language + age + cls_perc_eval
             + cls_students + cls_level + cls_profs + cls_credits + bty_avg, data = evals)
summary(m_final4)

