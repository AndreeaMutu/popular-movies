package com.andreea.popularmovies;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.andreea.popularmovies.model.Ingredient;
import com.andreea.popularmovies.model.Movie;
import com.andreea.popularmovies.model.Recipe;
import com.andreea.popularmovies.utils.JsonUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.andreea.popularmovies.utils.MovieConstants.MOVIES_LOADER_ID;
import static com.andreea.popularmovies.utils.MovieConstants.MOVIES_SORT_BY_KEY;
import static com.andreea.popularmovies.utils.MovieSortOrder.POPULAR;

/**
 * The configuration screen for the {@link NewAppWidget NewAppWidget} AppWidget.
 */
public class NewAppWidgetConfigureActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "com.andreea.popularmovies.NewAppWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    EditText mAppWidgetText;
    Spinner spinner;
    private List<Recipe> recipes;
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = NewAppWidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
            String widgetText = spinner.getSelectedItem().toString();
            List<Ingredient> ingredients = recipes.stream().filter(recipe -> recipe.getName().equals(widgetText)).findAny().map(Recipe::getIngredients).orElse(Collections.emptyList());
            StringBuilder ingredientsFormatter = new StringBuilder();
            for (Ingredient ingredient : ingredients) {
                ingredientsFormatter.append(String.format("\u2022 %s %s %s \n", ingredient.getQuantity(), ingredient.getMeasure(), ingredient.getIngredient()));
            }
            saveTitlePref(context, mAppWidgetId, widgetText);
            saveIngredientsPref(context, mAppWidgetId, widgetText, ingredientsFormatter.toString());

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            NewAppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };


    public NewAppWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    static void saveIngredientsPref(Context context, int appWidgetId, String recipe, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId+ recipe, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static String loadIngredientsPref(Context context, int appWidgetId, String recipe) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId+recipe, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.new_app_widget_configure);
        mAppWidgetText = (EditText) findViewById(R.id.appwidget_text);

        spinner = (Spinner) findViewById(R.id.appwidget_spinner);
        recipes = JsonUtils.parseRecipesJson();
        List<String> recipeNames = recipes.stream().map(Recipe::getName).collect(Collectors.toList());
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, recipeNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        mAppWidgetText.setText(loadTitlePref(NewAppWidgetConfigureActivity.this, mAppWidgetId));

    }


}

