Functions let us wrap up pieces of code so they can be used in lots of places. We can send data into functions to customize how they work, and get back data that tells us the result that was calculated.

Believe it or not, function calls used to be really slow. Steve Johnson, the author of many early coding tools for the Unix operating system, said this:

> “Dennis Ritchie (the creator of the C programming language) encouraged modularity by telling all and sundry that function calls were really, really cheap in C. Everybody started writing small functions and modularizing. Years later we found out that function calls were still expensive, and our code was often spending 50% of its time just calling them. Dennis had lied to us! But it was too late; we were all hooked...”

Why would they be “hooked” on function calls? Because they do so much to help simplify our code: rather than copying and pasting the same 10 lines of code in a dozen places, we can instead wrap them up in a function and use that instead. That means less code duplication, but also means if you change that function – perhaps adding more work – then everywhere using it will automatically get the new behavior, and there’s no risk of you forgetting to update one of the places you pasted it into.

**Today you have four tutorials to follow**, and you’ll learn how to write your own functions, how to accept parameters, and how to return data. Once you’ve completed each video you can go over any optional reading if you need it, then take a short test to help make sure you’ve understood what was taught.

1. [How to reuse code with functions](https://www.hackingwithswift.com/quick-start/beginners/how-to-reuse-code-with-functions)
    - Optional: [What code should be put in a function?](https://www.hackingwithswift.com/quick-start/understanding-swift/what-code-should-be-put-in-a-function)
    - Optional: [How many parameters should a function accept?](https://www.hackingwithswift.com/quick-start/understanding-swift/how-many-parameters-should-a-function-accept)
    - Test: [Writing functions](https://www.hackingwithswift.com/review/writing-functions)
    - Test: [Accepting parameters](https://www.hackingwithswift.com/review/accepting-parameters)
2. [How to return values from functions](https://www.hackingwithswift.com/quick-start/beginners/how-to-return-values-from-functions)
    - Optional: [When is the return keyword not needed in a Swift function?](https://www.hackingwithswift.com/quick-start/understanding-swift/when-is-the-return-keyword-not-needed-in-a-swift-function)
    - Test: [Returning values](https://www.hackingwithswift.com/review/returning-values)
3. [How to return multiple values from functions](https://www.hackingwithswift.com/quick-start/beginners/how-to-return-multiple-values-from-functions)
    - Optional: [Optional: When should you use an array, a set, or a tuple in Swift?](https://www.hackingwithswift.com/quick-start/understanding-swift/when-should-you-use-an-array-a-set-or-a-tuple-in-swift)
    - Test: [Tuples](https://www.hackingwithswift.com/review/tuples)
    - Test: [Arrays vs sets vs tuples](https://www.hackingwithswift.com/review/sixty/arrays-vs-sets-vs-tuples)
4. [How to customize parameter labels](https://www.hackingwithswift.com/quick-start/beginners/how-to-customize-parameter-labels)
    - Optional: [When should you omit a parameter label?](https://www.hackingwithswift.com/quick-start/understanding-swift/when-should-you-omit-a-parameter-label)
    - Test: [Omitting parameter labels](https://www.hackingwithswift.com/review/omitting-parameter-labels)