document.addEventListener("DOMContentLoaded", () => {
  const loginButton = document.getElementById("sign-in");

  loginButton.addEventListener("click", async () => {
    // Access the input fields by their 'name' attribute using querySelector
    const username = document.querySelector('input[name="username"]').value;
    const password = document.querySelector('input[name="password"]').value;

    try {
      const response = await fetch("http://localhost:3000/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password })
      });

      const result = await response.json();
      alert(result.message);

      // Optional redirect if login is successful
      if (result.success) {
        console.log("yaa")
        window.location.reload("/dashboard.html"); // make sure dashboard.html exists
      }
    } catch (error) {
      console.error("Login error:", error);
      alert("Login failed. Please try again.");
    }
  });
});
