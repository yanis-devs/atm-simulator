# Java Concurrency ATM Simulator

> A multithreaded console-based ATM simulation built in Java to demonstrate thread safety, synchronization, and race condition handling in banking operations.

---

## Features
* **Multithreaded Simulation:** Simulates two independent ATM machines (`ATM-Machine-1` and `ATM-Machine-2`) trying to withdraw funds from the exact same bank account simultaneously.
* **Data Race Demonstration:** Configurable toggle (`USE_SYNCHRONIZATION`) to showcase inconsistent states/race conditions versus thread-safe execution.
* **Input Validation:** Robust error-handling to filter out invalid characters, negative numbers, and overdraft attempts.

---
