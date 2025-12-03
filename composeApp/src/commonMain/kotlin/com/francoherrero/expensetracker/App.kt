package com.francoherrero.expensetracker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.francoherrero.expensetracker.navigation.AddExpenseDestination
import com.francoherrero.expensetracker.navigation.ExpenseDetailDestination
import com.francoherrero.expensetracker.navigation.ExpenseListDestination
import com.francoherrero.expensetracker.presentation.controller.UIEvent
import com.francoherrero.expensetracker.presentation.controller.UIEventController
import com.francoherrero.expensetracker.ui.expense.AddExpenseRoute
import com.francoherrero.expensetracker.ui.expense.ExpenseDetailRoute
import com.francoherrero.expensetracker.ui.expense.ExpenseListRoute
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.AngleLeft
import kotlin.math.exp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    MaterialTheme {
        Surface {
            val navController = rememberNavController()
            val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
            val uiEvents = remember { UIEventController() }

            fun handleAddExpenseDone() {
                uiEvents.showSnackbar("Expense added")
                navController.navigate(ExpenseListDestination)
            }

            fun handleDelete() {
                uiEvents.showSnackbar("Expense deleted")
                navController.navigate(ExpenseListDestination)
            }

            LaunchedEffect(Unit) {
                uiEvents.events.collect { event ->
                    when (event) {
                        is UIEvent.ShowSnackbar -> {
                            snackbarHostState.showSnackbar(event.message)
                        }
                    }
                }
            }

            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState)},
                floatingActionButton = {
                    FloatingActionButton(onClick = {
                        navController.navigate(AddExpenseDestination)
                    }) {
                        Text("+")
                    }
                },
                topBar = {
                    val cd by navController.currentBackStackEntryAsState()
                    val title = when (cd?.destination?.route) {
                        "com.francoherrero.expensetracker.navigation.ExpenseListDestination"-> "Expenses"
                        "com.francoherrero.expensetracker.navigation.AddExpenseDestination"-> "Add Expense"
                        "com.francoherrero.expensetracker.navigation.ExpenseDetailDestination"-> "Expense details"
                        else -> ""
                    }

                    TopAppBar(
                        title = { Text(text = title) },
                        navigationIcon = {
                            if (cd?.destination?.route != "com.francoherrero.expensetracker.navigation.ExpenseListDestination") {
                                IconButton(onClick = {
                                    navController.popBackStack()
                                }) {
                                    Icon(
                                        imageVector = FontAwesomeIcons.Solid.AngleLeft,
                                        contentDescription = "back icon"
                                    )
                                }
                            }
                        }
                    )
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues)
                ) {

                    NavHost(
                        navController = navController,
                        startDestination = ExpenseListDestination
                    ) {
                        composable<AddExpenseDestination> {
                            AddExpenseRoute(
                                modifier = Modifier.padding(paddingValues),
                                onDone = ::handleAddExpenseDone
                            )
                        }
                        composable<ExpenseListDestination> {
                            ExpenseListRoute(
                                onAddClick = {
                                    navController.navigate(AddExpenseDestination)
                                },
                                onExpenseClick = { expenseId ->
                                    navController.navigate(ExpenseDetailDestination(expenseId))
                                }
                            )
                        }
                        composable<ExpenseDetailDestination> { backstackEntry ->
                            val route = backstackEntry.toRoute<ExpenseDetailDestination>()

                            ExpenseDetailRoute(expenseId = route.expenseId, onDelete = ::handleDelete)
                        }
                    }
                }
            }
        }
    }
}
