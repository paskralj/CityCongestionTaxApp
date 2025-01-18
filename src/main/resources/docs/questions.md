# Notes of thoughts and questions

## Before starting a project
- Time limit, prioritize tasks (eliminate unnecessary ones)
- Conduct requirements analysis
- The key is to design the logic for calculating taxes per day and retrieve it via the controller, if there is time to unit test and integration tests
- Find out what Congestion tax is and how it is calculated

  - Assumptions/questions:
    - I assume that the user printed a timestamp for only one car in 2013 (format: Timestamp)
    - I assume that a colleague has done plate recognition and is writing them into a database, so that he can use the plate to identify what type of car it is, or whether the toll is applicable to that car.


## Tax calculation logic per day
- As far as I found out by googling, city taxes are charged via camera/sensor regardless of the direction in which the cars are coming (entrance exit)
- If the sensor passes occur within an hour, only the most expensive tariff determined by each city is taken into account.
- I assume that a colleague printed the timestamps for passes through the sensor/camera only for one car in 2013
- On the basis of this data, a colleague did a "manual" analysis and preparations, according to which he will create functionality related to the calculation of daily taxes. I assume that the colleague planned to use the solutions to these analyses as a check to see if he had done the logic/code correctly.
- I assume that an analysis has already been done according to registrations, whether the car is exempt from paying taxes


## Controller
- From the car registration/carType and city, you can get carTimeStampsWithinADay and congestionTaxRulesList. We are limited by time, I would add this logic as an improvement in Services. I will simulate this data, so I don't do any additional logic.

## Improvements
  ### Logical part
- Maybe some cities don't want a 60-minute single tax rule. We can put that as an option. Maybe another city doesn't want 60 minutes but some other number - and enable that.
- Currently, only tax calculation for one day is implemented, but we can calculate for multiple time intervals (e.g. year, month, etc.)
- Add a method/check/logic that it should not calculate tax for the current day, at least until it reaches the "free part" when the tax is zero
- Add logic to check congestionTaxRulesList, i.e. if the city sets the prices incorrectly and we want to be charged for an "empty" period that is not defined. For example, the city has set the charge from 6:00-6:15 and 6:30-6:45 and we pass at 6:20

  ### Technical part
- Add and implement integration tests
- Test more corner cases in UnitTest (e.g. odd passes per day ...)
- Depending on what in the controller we want to add those options in the service and repository layer. For example, get a request for the price of a year...
- Throw out "directly" hardcoded parameters
- Add more enums
- Add "custom" exceptions

## Return of a colleague
- When a colleague returns from work, show him the code and what to pay attention to when adapting and customizing it.