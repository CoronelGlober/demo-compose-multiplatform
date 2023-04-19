//
//  ContentView.swift
//  iOS Client
//
//  Created by David Alejandro Coronel Baracaldo on 18/04/23.
//

import SwiftUI
import common

struct ComposableClientScreen: View {
    var body: some View {
        ComposableView().ignoresSafeArea(.keyboard)
    }
}

struct ComposableView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        IOSClientScreenKt.MainScreenViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ComposableClientScreen()
    }
}
