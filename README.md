# Coronavirus-tracker

Built using guide from Java Brains: https://www.youtube.com/watch?v=8hjNG9GZGnQ

Uses the following: 

- Spring framework with Maven dependency management
- Thymeleaf templates
- Apache commons
- Bootstrap for CSS layout

- Data from CSV file on github: https://github.com/CSSEGISandData/COVID-19/tree/master/csse_covid_19_data/csse_covid_19_time_series
    -> Pulled via @Scheduled @Service using @PostConstruct 
- @Controller class using @Autowired to manage dependency injection and @GetMapping in conjunction with Thymeleaf



Notes from end of the video:

1. Avoid saving state/values in Spring services

    -> Services should typically be stateless

2. Write tests for your code!
 
    -> Test cases should be written for fetching data for example

3. Data validity depends on the source
