package indigo

const val DIAMONDS = "\u2666"
const val HEARTS = "\u2665"
const val SPADES = "\u2660"
const val CLUBS = "\u2663"
const val ONE_HAND = 6
const val STARTING_TABLE_CARDS = 4

val cardRanks = listOf<Any>('A', '2', '3', '4', '5', '6', '7', '8', '9', "10", 'J', 'Q', 'K')
val cardSuits = listOf(DIAMONDS, HEARTS, SPADES, CLUBS)

/** A class that resembles a deck of cards. */
class CardDeck(cardRanks: List<Any>, cardSuits: List<String>) {
    var cardsInDeck = mutableListOf<String>()
    init {
        for (suit in cardSuits) {
            for (rank in cardRanks) {
                // print("$rank$suit ")
                cardsInDeck.add("$rank$suit")
            }
        }
    }

    /** Reset the order of the cards to be organized by suit from A to K. */
    fun reset() {
        cardsInDeck.clear()
        for (suit in cardSuits) {
            for (rank in cardRanks) {
                cardsInDeck.add("$rank$suit")
            }
        }
        println("Card deck is reset.")
    }

    /** Shuffle the cards to mix up the order of the cards. */
    fun shuffleDeck() {
        for ((deckIndex, card) in cardsInDeck.shuffled().withIndex()) {
            cardsInDeck[deckIndex] = card
        }
        println("Card deck is shuffled.")
    }

    /** Remove a number of cards from the deck and return a list of all cards that have been removed from the deck. */
    fun removeCardsFromDeck(cardsToRemove: Int): List<String> {
        // Completely refactored because players will not be asking for specific numbers of cards.
        // println("Number of cards:")
        // Using toIntoOrNull to handle user input that can't be cast to Int.
        // When cardsToGet can't be converted to Int, a default value of -1 is set so the else block can be reached.
        // val cardsToGet = readln().toIntOrNull() ?: -1
//        if ((cardsToDeal > 0) && (cardsToDeal < 53)) {
//            if (cardsToDeal > cardsInDeck.size) {
//                println("The remaining cards are insufficient to meet the request.")
//            } else {
                val takenCards = cardsInDeck.take(cardsToRemove)
                cardsInDeck.removeAll(cardsInDeck.take(cardsToRemove))
                return takenCards
                // No longer have to display taken cards as they go to the table or a player.
                // for (card in takenCards) {
                //    print("$card ")
                // }
//            }
//        } else {
//            println("Invalid number of cards.")
//        }
    }

    /** Display all card ranks, Ace through King. */
    fun displayRanks() {
        // Print all ranks
        for (rank in cardRanks) {
            print("$rank ")
        }
        println()
    }

    /** Display all card suits: Diamonds, Hearts, Spades, Clubs. */
    fun displaySuits() {
        // Print all suits.
        for (suit in cardSuits) {
            print("$suit ")
        }
        println()
    }

    /** Shuffle all cards in the deck and then display them. */
    fun displayShuffledDeck() {
        for (suit in cardSuits.shuffled()) {
            for (rank in cardRanks.shuffled()) {
                print("$rank$suit ")
            }
        }
    }
}

/** A class used to manage the game for players. Equivalent to a dealer in a real life card game. */
class GameHandler() {
    var keepPlaying = false
    private var identifiedFirstPlayer = false

    /** Print the title of the game. */
    fun printGameTitle() {
        println("Indigo Card Game")
    }

    /** Set either playerOne or playerTwo as the first player for the game. */
    fun decideFirstPlayer(playerOne: Player, playerTwo: Player) {
        while (!identifiedFirstPlayer) {
            println("Play first?")
            val playerAnswer = readln()
            if (playerAnswer.lowercase() == "yes") {
                playerOne.isFirstPlayer = true
                playerTwo.isFirstPlayer = false
                identifiedFirstPlayer = true
            } else if (playerAnswer.lowercase() == "no") {
                playerOne.isFirstPlayer = false
                playerTwo.isFirstPlayer = true
                identifiedFirstPlayer = true
            }
        }
    }

    /** Display a list of actions that the user can choose from:
     * Actions are: reset, shuffle, get, exit, look
     */
    fun displayActionMenu(deck: CardDeck, table: Table, playerOne: Player, playerTwo: Player) {
        println("Choose an action (reset, shuffle, get, exit):")
        val playerChoice = readln()
        if (playerChoice == "reset") {
            deck.reset()
        } else if (playerChoice == "shuffle") {
            deck.shuffleDeck()
        } else if (playerChoice == "get") {
            deck.removeCardsFromDeck(0)
        } else if (playerChoice == "exit") {
            keepPlaying = exitGame()
        } else if (playerChoice == "look") {
            lookAtCards(deck, table, playerOne, playerTwo)
        } else {
            println("Wrong action.")
        }
    }

    /** Deal cards from the deck to the table and both players. */
    fun dealCards(deck: CardDeck, table: Table, playerOne: Player, playerTwo: Player) {
        table.cardsOnTable.addAll(deck.removeCardsFromDeck(STARTING_TABLE_CARDS))
        playerOne.cardsInHand.addAll(deck.removeCardsFromDeck(ONE_HAND))
        playerTwo.cardsInHand.addAll(deck.removeCardsFromDeck(ONE_HAND))
    }

    /** Deal cards from the deck to both players. */
    fun dealCards(deck: CardDeck, playerOne: Player, playerTwo: Player) {
        playerOne.cardsInHand.addAll(deck.removeCardsFromDeck(ONE_HAND))
        playerTwo.cardsInHand.addAll(deck.removeCardsFromDeck(ONE_HAND))
    }

    /** Display all cards in the game and where they currently are. */
    private fun lookAtCards(deck: CardDeck, table: Table, playerOne: Player, playerTwo: Player) {
        print("Cards remaining in the deck: ")
        displayCards(deck.cardsInDeck)
        print("Cards on the table: ")
        displayCards(table.cardsOnTable)
        print("Cards in Player One's hand: ")
        displayCards(playerOne.cardsInHand)
        print("Cards in Player Two's hand: ")
        displayCards(playerTwo.cardsInHand)
    }

    /** Display all cards for any set of cards. */
    fun displayCards(cards: MutableList<String>) {
        for (card in cards) {
            print("$card ")
        }
        println()
    }

    /** End the game and exit the program. */
    fun exitGame(): Boolean {
        println("Game Over")
        return true
    }

    /** Announce and display the initial set of cards on the table. */
    fun beginPlay(table: Table) {
        print("Initial cards on the table: ")
        displayCards(table.cardsOnTable)
        println()
    }

    /** Announce the number of cards on the table, and display the top card on the table. */
    fun announceCardsOnTable(table: Table) {
        print("${table.cardsOnTable.size} cards on the table, and the top card is ${table.cardsOnTable.last()}")
        println()
    }

}

/** A class used to manage player cards and actions. */
class Player(var playerType: Boolean) {
    var cardsInHand = mutableListOf<String>()
    var isFirstPlayer = true
    var isHumanPlayer: Boolean

    init {
        if (playerType) {
            isHumanPlayer = true
        } else {
            isHumanPlayer = false
        }
    }

    /** Display a numbered list of the current set of cards in a player's hand. */
    fun checkCardsInHand() {
        var cardNumber = 0
        print("Cards in hand: ")
        for (card in cardsInHand) {
            print("${++cardNumber})$card ")
        }
        println()
    }

    /** Remove a card from the player's hand and add it to the set of cards on the table. */
    fun playACard(table: Table, gameHandler: GameHandler) {
        var cardPlayed = false

        if (isHumanPlayer) {
            while (!cardPlayed) {
                println("Choose a card to play (1-${cardsInHand.size}):")
                val playerInput = readln()
                if (playerInput == "exit") {
                    gameHandler.keepPlaying = gameHandler.exitGame()
                    break
                } else {
                    val cardChoice = playerInput.toIntOrNull() ?: -1
                    if ((cardChoice > 0) && (cardChoice <= cardsInHand.size)) {
                        table.cardsOnTable.add(cardsInHand.elementAt(cardChoice - 1))
                        cardsInHand.removeAt(cardChoice - 1)
                        cardPlayed = true
                    }
                }
            }
        } else {
            table.cardsOnTable.add(cardsInHand.elementAt(0))
            cardsInHand.removeAt(0)
            println("Computer plays ${table.cardsOnTable.last()}")
        }
        println()
    }
}

/** A class used to manage cards on a table. */
class Table() {
    var cardsOnTable = mutableListOf<String>()
}

/** Run the main Indigo Card Game program. */
fun main() {
    val cardDeck = CardDeck(cardRanks, cardSuits)
    val playerOne = Player(true)
    val playerTwo = Player(false)
    val table = Table()
    val gameHandler = GameHandler()

    gameHandler.printGameTitle()
    gameHandler.decideFirstPlayer(playerOne, playerTwo)
    println("P1 first: ${playerOne.isFirstPlayer}, P2 first: ${playerTwo.isFirstPlayer}")
    println("P1 human: ${playerOne.isHumanPlayer}, P2 human: ${playerTwo.isHumanPlayer}")
    gameHandler.dealCards(cardDeck, table, playerOne, playerTwo)
    // gameHandler.displayActionMenu(cardDeck, table, playerOne, playerTwo)
    gameHandler.beginPlay(table)

    if (playerTwo.isFirstPlayer) {
        gameHandler.announceCardsOnTable(table)
        playerTwo.playACard(table, gameHandler)
    }

    while (!gameHandler.keepPlaying) {
        if ((playerOne.cardsInHand.size == 0) && (playerTwo.cardsInHand.size == 0)) {
            if (cardDeck.cardsInDeck.size == 0) {
                gameHandler.keepPlaying = gameHandler.exitGame()
            } else {
                gameHandler.dealCards(cardDeck, playerOne, playerTwo)
            }
        } else {
            gameHandler.announceCardsOnTable(table)
            playerOne.checkCardsInHand()
            playerOne.playACard(table, gameHandler)
            if (gameHandler.keepPlaying) {
                break
            } else {
                gameHandler.announceCardsOnTable(table)
                playerTwo.playACard(table, gameHandler)
            }
        }
    }
}