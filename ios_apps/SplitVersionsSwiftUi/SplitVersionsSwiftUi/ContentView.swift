//
//  ContentView.swift
//  SplitVersionsSwiftUi
//
//  Created by Luis Enrique Mendez Cantero on 14.01.25.
//

import SwiftUI

struct ContentView: View {
    @State private var appEnvironment: String?
    @State private var appColor: String?
    
    init() {
        if let appEnvironment = Bundle.main.object(forInfoDictionaryKey: "AppEnvironment") as? String {
                    _appEnvironment = State(initialValue: appEnvironment)
                    print("App Environment: \(appEnvironment)")
                } else {
                    print("App Environment key not found in Info.plist")
                }
        
        if let appColor = Bundle.main.object(forInfoDictionaryKey: "AppColor") as? String {
                    _appColor = State(initialValue: appColor)
                    print("App Color: \(appColor)")
                } else {
                    print("App Color key not found in Info.plist")
                }
    }

    var body: some View {
        VStack {
            Color(appColor == "blue" ? Color.blue : Color.red )
            Image(systemName: "globe")
                .imageScale(.large)
                .foregroundStyle(.tint)
            
            Text("Hello, world! Environment: \(appEnvironment ?? "Unknown")")
        }
        .padding()
    }
}

