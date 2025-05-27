Project Title: Flingo
Project : Language Flashcard App


The Language Flashcard App is a console or GUI-based Java application that helps users learn new vocabulary in a target language (like Spanish, Hindi, French, etc.) using digital flashcards. Each flashcard has a question side (word in native language) and an answer side (word in the target language). The app supports multiple decks (e.g., "Basic Words", "Phrases", "Food Items") and tracks user performance over time.

This project showcases core OOP principles:

Encapsulation (data like word, meaning, and success rate hidden inside objects)

Inheritance (e.g., different flashcard types: TextFlashcard, ImageFlashcard)

Polymorphism (quiz methods behave differently based on card type)

Abstraction (interface like Practiceable for different learning strategies)



Use Case Scenarios:

1. Vocabulary Practice:

User selects a deck (e.g., "Common Verbs")

The app shows the word in English, and the user must type the word in Spanish

It gives immediate feedback and tracks right/wrong answers


2. Learning Progress Tracker:

Stores how often a flashcard was answered correctly

Highlights "weak" flashcards for more practice


3. Custom Deck Creation:

Users can create their own flashcard decks

Add, delete, or edit flashcards


4. Quiz Modes:

Normal Mode: Show word → guess translation

Reverse Mode: Show translation → guess word

Timed Mode: Answer within time limit



---

Potential Classes:

Flashcard (abstract base class)

Fields: question, answer, attempts, correctCount


TextFlashcard extends Flashcard

Deck (holds list of Flashcard)

UserProfile (name, decks created, accuracy)

FlashcardManager (create/edit/delete flashcards)

PracticeSession (controls quiz flow)
