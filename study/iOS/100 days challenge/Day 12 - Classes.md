At first, classes seem very similar to structs because we use them to create new data types with properties and methods. However, they introduce a new, important, and _complex_ feature called inheritance – the ability to make one class build on the foundations of another.

This is a powerful feature, there’s no doubt about it, and there is no way to avoid using classes when you start building real iOS apps. But please remember to keep your code simple: just because a feature exists, it doesn’t mean you need to _use_ it. As Martin Fowler wrote, “any fool can write code that a computer can understand, but good programmers write code that humans can understand.”

I’ve already said that SwiftUI uses structs extensively for its UI design. Well, it uses _classes_ extensively for its data: when you show data from some object on the screen, or when you pass data between your layouts, you’ll usually be using classes.

I should add: if you’ve ever worked with UIKit before, this will be a remarkable turnaround for you – in UIKit we normally use classes for UI design and structs for data. So, if you thought perhaps you could skip a day here and there I’m sorry to say that you can’t: this is all required.

**Today you have six tutorials to work through, and you’ll meet classes, inheritance, deinitializers, and more.** Once you’ve watched each video and completed any optional extra reading you wanted, there are short tests to help make sure you’ve understood what was taught.

1. [How to create your own classes](https://www.hackingwithswift.com/quick-start/beginners/how-to-create-your-own-classes)
    - Optional: [Why does Swift have both classes and structs?](https://www.hackingwithswift.com/quick-start/understanding-swift/why-does-swift-have-both-classes-and-structs)
    - Optional: [Why don’t Swift classes have a memberwise initializer?](https://www.hackingwithswift.com/quick-start/understanding-swift/why-dont-swift-classes-have-a-memberwise-initializer)
    - Test: [Creating your own classes](https://www.hackingwithswift.com/review/sixty/creating-your-own-classes)
2. [How to make one class inherit from another](https://www.hackingwithswift.com/quick-start/beginners/how-to-make-one-class-inherit-from-another)
    - Optional: [When would you want to override a method?](https://www.hackingwithswift.com/quick-start/understanding-swift/when-would-you-want-to-override-a-method)
    - Optional: [Which classes should be declared as final?](https://www.hackingwithswift.com/quick-start/understanding-swift/which-classes-should-be-declared-as-final)
3. [How to add initializers for classes](https://www.hackingwithswift.com/quick-start/beginners/how-to-add-initializers-for-classes)
    - Test: [Class inheritance](https://www.hackingwithswift.com/review/sixty/class-inheritance)
4. [How to copy classes](https://www.hackingwithswift.com/quick-start/beginners/how-to-copy-classes)
    - Optional: [Why do copies of a class share their data?](https://www.hackingwithswift.com/quick-start/understanding-swift/why-do-copies-of-a-class-share-their-data)
    - Test: [Copying objects](https://www.hackingwithswift.com/review/sixty/copying-objects)
5. [How to create a deinitializer for a class](https://www.hackingwithswift.com/quick-start/beginners/how-to-create-a-deinitializer-for-a-class)
    - Optional: [Why do classes have deinitializers and structs don’t?](https://www.hackingwithswift.com/quick-start/understanding-swift/why-do-classes-have-deinitializers-and-structs-dont)
    - Test: [Deinitializers](https://www.hackingwithswift.com/review/sixty/deinitializers)
6. [How to work with variables inside classes](https://www.hackingwithswift.com/quick-start/beginners/how-to-work-with-variables-inside-classes)
    - Optional: [Why can variable properties in constant classes be changed?](https://www.hackingwithswift.com/quick-start/understanding-swift/why-can-variable-properties-in-constant-classes-be-changed)
    - Test: [Mutability](https://www.hackingwithswift.com/review/sixty/mutability)
7. [Summary: Classes](https://www.hackingwithswift.com/quick-start/beginners/summary-classes)
8. [Checkpoint 7](https://www.hackingwithswift.com/quick-start/beginners/checkpoint-7)