import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.neural_network import MLPRegressor
from sklearn.metrics import mean_absolute_error

# Load the dataset
df = pd.read_csv('bikes.csv')

# Separate features (X) and target (y)
X = df.drop(columns=['next_hour'])
y = df['next_hour']

# Split the data into training and test sets (80/20) without shuffling
train_size = int(0.8 * len(df))
X_train, X_test, y_train, y_test = train_test_split(
    X, y,
    test_size=0.2,
    shuffle=False,
    random_state=42
)

# Scale the input features
scaler = StandardScaler()
X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.transform(X_test)

# Train the MLPRegressor
mlp = MLPRegressor(
    hidden_layer_sizes=(32, 32),
    max_iter=512,
    random_state=42
)
mlp.fit(X_train_scaled, y_train)

# Predict on the test set
y_pred = mlp.predict(X_test_scaled)

# Calculate and print the mean absolute error
mae = mean_absolute_error(y_test, y_pred)
print(f'Mean Absolute Error: {mae:.4f}')