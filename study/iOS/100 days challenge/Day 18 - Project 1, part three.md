You just finished building your first SwiftUI app, and all being well you were surprised by how easy it was. You might even be wondering why I spent so long talking about structs, closures, optionals, and more, when really we ended up doing some fairly simple code.

Well, first keep in mind that this is only the first project, and I kept it deliberately simple so you can get moving quickly with your own code. Trust me, things will get more complex – in fact tomorrow you’ll be set free with your own project, so don’t get too comfortable!

Second, though, you _have_ been using advanced features. In fact, without realizing it you’ve actually been using all the most advanced features Swift has to offer. We’ve actually been using closures all the time – just look at code like this:

```swift
Picker("Tip percentage", selection: $tipPercentage) {
    ForEach(tipPercentages, id: \.self) {
        Text($0, format: .percent)
    }
}
```

Did you notice that `$0` in there? That’s shorthand syntax for closure parameters, because we’re inside a closure.

Anyway, now that the app is complete it’s time for a quick recap what you learned, a short test to make sure you’ve understood what was taught, then your first challenges – exercises designed to get you writing your own code as quickly as possible.

**I do not provide the answers to these challenges.** This is intentional: I want you to figure it out by yourself rather than just looking at someone else’s work. As Marvin Phillips said, “the difference between try and triumph is a little umph.”

**Today you should work through the wrap up chapter for project 1, complete its review, then work through all three of its challenges.**

- [WeSplit: Wrap up](https://www.hackingwithswift.com/books/ios-swiftui/wesplit-wrap-up)
- [Review for Project 1: WeSplit](https://www.hackingwithswift.com/review/ios-swiftui/wesplit)

Once you’re done, tell other people: you’ve completed your first SwiftUI project, you’ve passed the test, and you’ve even extended it with your own code.

You should be proud of what you’ve accomplished – good job!