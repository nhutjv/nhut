
/* eslint-disable no-undef */
/* eslint-disable no-restricted-globals */

importScripts('https://www.gstatic.com/firebasejs/9.10.0/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/9.10.0/firebase-messaging-compat.js');

const config1 = {
  apiKey: "AIzaSyB3r64mjDNh5avpeSBELfnb83KuOjl-9bw",
  authDomain: "pushdemo-6b5fe.firebaseapp.com",
  projectId: "pushdemo-6b5fe",
  storageBucket: "pushdemo-6b5fe.appspot.com",
  messagingSenderId: "1081310369615",
  appId: "1:1081310369615:web:27674bba35312908bc973a",
  measurementId: "G-K6DQHQKGKC"
};

// Khởi tạo Firebase trong Service Worker
firebase.initializeApp(config1);
const messaging = firebase.messaging();







