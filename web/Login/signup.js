document.addEventListener('DOMContentLoaded', () => {
  const signUpBtn = document.getElementById('sign-up');

  signUpBtn.addEventListener('click', async () => {
    const inputs = document.getElementsByClassName('inputs');
    const username = inputs[0].value.trim();
    const password = inputs[1].value.trim();

    // Validate inputs before sending to server
    if (!username || !password) {
      alert('Please enter both username and password.');
      return;
    }

    try {
      const response = await fetch('http://localhost:3000/signup', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password })
      });

      const contentType = response.headers.get('content-type');

      if (contentType && contentType.includes('application/json')) {
        const result = await response.json();

        if (response.ok) {
          alert(result.message || 'Signup successful!');
          // Redirect to login page
          window.location.href = '/index.html';
        } else {
          alert(result.error || 'Signup failed.');
        }
      } else {
        const text = await response.text();
        console.error('Expected JSON but got:', text);
        alert('Unexpected response from server.');
      }
    } catch (err) {
      console.error('Signup error:', err);
      alert('Failed to signup. Please try again later.');
    }
  });
});
