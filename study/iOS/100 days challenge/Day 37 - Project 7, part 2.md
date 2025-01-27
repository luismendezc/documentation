Today you’re going to build a complete app using `@Observable`, `sheet()`, `Codable`, `UserDefaults`, and more. I realize that seems like a lot, but I want you to try to think about all the things that are happening in the background:

- `@Observable` watches classes for changes and refreshes any views that are affected.
- `sheet()` watches a condition we specify and shows or hides a view automatically.
- `Codable` can convert Swift objects into JSON and back with almost no code from us.
- `UserDefaults` can read and write data so that we can save settings and more instantly.

Yes, we need to write the code to put those things in place, but so much boilerplate code has been removed that what remains is quite remarkable. As French writer and poet Antoine de Saint-Exupery once said, “perfection is achieved not when there is nothing more to add, but rather when there is nothing more to take away.”

**Today you have five topics to work through, in which you’ll put into practice everything you learned about `@Observable`, `sheet()`, `onDelete()`, and more.**

- [Building a list we can delete from](https://www.hackingwithswift.com/books/ios-swiftui/building-a-list-we-can-delete-from)
- [Working with Identifiable items in SwiftUI](https://www.hackingwithswift.com/books/ios-swiftui/working-with-identifiable-items-in-swiftui)
- [Sharing an observed object with a new view](https://www.hackingwithswift.com/books/ios-swiftui/sharing-an-observed-object-with-a-new-view)
- [Making changes permanent with UserDefaults](https://www.hackingwithswift.com/books/ios-swiftui/making-changes-permanent-with-userdefaults)
- [Final polish](https://www.hackingwithswift.com/books/ios-swiftui/final-polish)