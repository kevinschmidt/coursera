library(ggplot2)
str(mpg)
qplot(displ, hwy, data=mpg)
qplot(displ, hwy, data=mpg, shape=drv)
qplot(displ, hwy, data=mpg, color=drv)
qplot(displ, hwy, data=mpg, color=drv, geom=c("point", "smooth"))
qplot(displ, hwy, data=mpg, color=drv, geom=c("point", "smooth"), method="lm")
qplot(hwy, data=mpg, fill=drv)

qplot(displ, hwy, data=mpg, color=drv, facets= .~ drv, geom=c("point", "smooth"))
qplot(hwy, data=mpg, facets=drv ~., binwith=2, fill=drv)
qplot(displ, hwy, data=mpg, color=drv, facets= class ~ drv, geom=c("point", "smooth"))

qplot(hwy, data=mpg, color=drv, geom="density")