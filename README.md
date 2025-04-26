# 🛒 Shopping List App

<img src="https://img.shields.io/badge/Kotlin-1.9.20-blue?logo=kotlin" alt="Kotlin Version"> 
<img src="https://img.shields.io/badge/Android-API_24%2B-brightgreen?logo=android" alt="Android API"> 
<img src="https://img.shields.io/github/last-commit/VladislavRubashkin/ShoppingList" alt="Last Commit">

Modern Android shopping list application built with cutting-edge technologies to manage your purchases efficiently.

## ✨ Features

- **CRUD Operations**: Create, Read, Update, Delete shopping items
- **Real-time Updates**: Immediate UI synchronization with database changes
- **Data Validation**: Smart input checking for names and quantities
- **State Management**: Sealed classes for robust UI state handling
- **Modern Arch**: MVVM with Clean Architecture principles

## 🛠 Tech Stack

| Category          | Technologies                                                                |
|-------------------|-----------------------------------------------------------------------------|
| Language          | Kotlin                                                                      |
| Architecture      | MVVM, Repository Pattern                                                    |
| DI                | Dagger2                                                                     |
| Async             | Coroutines + Flow                                                           |
| Persistence       | Room Database                                                               |
| UI                | XML                                                                         |


## 🚀 Getting Started

### Prerequisites
- Android Studio Giraffe+
- JDK 17
- Android SDK 34

### Installation
```bash
git clone https://github.com/VladislavRubashkin/ShoppingList.git
cd ShoppingList

🏗 Project Structure

com/
└── vladislavrubashkin/
    └── shoppinglist/
        ├── di/           # Dependency Injection
        ├── domain/        # Business logic
        │   ├── model/
        │   └── use_case/  
        ├── data/          # Data layer
        │   ├── database/  # Room entities
        │   ├── mapper/    # Data mapping
        │   └── repository/
        └── presentation/  # UI layer
            ├── screen/    # Activities/Fragments
            ├── adapter/   # RecyclerView adapters
            └── viewmodel/ # ViewModels

🧪 Testing Strategy

Test Type	  Coverage
Unit Tests	ViewModels, UseCases, Repository

📬 Contact

Vladislav Rubashkin - RubashkinVlad@yandex.ru

Project Link: https://github.com/VladislavRubashkin/ShoppingList
