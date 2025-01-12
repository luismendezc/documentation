Today you’re going to face your first challenge day, which is a day where you’re asked to build a completely new app from scratch using what you’ve learned so far. This might come as a surprise to you, because you may well think you haven’t learned that much so far. Well, let me explain…

The 100 Days of SwiftUI is the second 100 Days curriculum I’ve written, and while I know the original [100 Days of Swift](https://www.hackingwithswift.com/100) was extremely popular and helped a lot of people, I definitely felt afterwards “if I were to do it all again, here’s what I’d change…”

One of those things was going back and adding more virtual chats to the initial Swift days, to help folks really make the most of those fundamentals. But the _second_ change is one you’re meeting today: I want to get you writing your own projects faster.

In the original 100 Days folks completed the first project, then the second, then the third, and only then were asked to write their own app from scratch. While that worked well enough, I realized in retrospect that going on to a second and third project without really having solidified the basics wasn’t ideal.

And that brings us to today: your first challenge day, where you’re going to build a complete app from scratch. Don’t worry: this project has specifically been chosen based on what you’ve learned so far, so everything you need to know was covered already in project 1.

Hacking with Swift+ subscribers can get a complete video solution for this checkpoint here: **[Solution to Challenge 1](https://www.hackingwithswift.com/plus/solutions/challenge-1-converter)**. As well as solving the challenge, we also walk through two awesome extensions making it do even more! If you don’t already subscribe, you can start a free trial today.

## Your challenge

You need to build an app that handles unit conversions: users will select an input unit and an output unit, then enter a value, and see the output of the conversion.

Which units you choose are down to you, but you could choose one of these:

- Temperature conversion: users choose Celsius, Fahrenheit, or Kelvin.
- Length conversion: users choose meters, kilometers, feet, yards, or miles.
- Time conversion: users choose seconds, minutes, hours, or days.
- Volume conversion: users choose milliliters, liters, cups, pints, or gallons.

If you were going for length conversion you might have:

- A segmented control for meters, kilometers, feet, yard, or miles, for the input unit.
- A second segmented control for meters, kilometers, feet, yard, or miles, for the output unit.
- A text field where users enter a number.
- A text view showing the result of the conversion.

So, if you chose meters for source unit and feet for output unit, then entered 10, you’d see 32.81 as the output.

**You know almost everything you need to build this.** In fact, if you go for a simple solution – and I hope you do – the only actually new thing you’ll find is that you need to use a different `format` option for your text field, because `.currency` and `.percent` don’t make sense here. You should choose `.number` instead, which is suitable for standard numerical input.

## Tips

You know everything you need to complete this project, but if you’re hitting problems then I have some tips that might help.

First, and most importantly: keep it simple! You’ll have lots of time in your future career to think up clever coding architectures and more, but in this initial challenge I want you to go for the simplest solution you can find.

Second, all our unit conversions are simple mathematics, but you shouldn’t try to write conversions to go from every source unit to very other unit. A better idea is to convert the user’s input to a single base unit, then convert from there to the target unit.

So, rather than writing code to convert from liters to milliliters, and from liters to cups, and from liters to pints, and so on, a better idea is to convert the user’s input into milliliters (the lowest common denominator), then convert from there to whatever output unit they want.

Third, as we have three user values here – their input number, their input unit, and their output unit – you need to have three `@State` properties to store them all. You’ll need a `TextField`, two pickers, and a text view to show your output, and that’s about it. You can break your form up into sections if you want, but it’s not required.

Fourth, the easiest way to store your conversion units is to have a simple string array, which you can loop over using something like `ForEach(units, id: \.self)`.

Finally, if you’d like to be a bit fancy with the output number you display, try calling `.formatted()` on it – e.g. `someDouble.formatted()`. This will cause iOS to format the number for printing, adding thousands separator and removing a lot of the noise from unnecessary decimal places.

Go ahead and get started now. Don’t worry if you need to refer back to the WeSplit project – that’s perfectly normal, and is all part of the learning process.

Good luck! **You can do this.** And once you’re done, tell other people: you’ve built another SwiftUI app, and this one was entirely designed by you.

You should be proud of what you’ve accomplished.

**Tip:** Even though you can absolutely complete this project using simple arithmetic, you might be interested to know that Apple does give us dedicated functionality for doing unit conversion – see my article _[How to convert units using Unit and Measurement](https://www.hackingwithswift.com/example-code/system/how-to-convert-units-using-unit-and-measurement)_ if you’re curious. However, I should reiterate that it is _not_ required to complete this project: you can convert from liters to pints (for example) just by multiplying the input by 2.11338.