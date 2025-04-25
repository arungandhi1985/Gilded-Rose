// jest-dom adds custom jest matchers for asserting on DOM nodes.
// allows you to do things like:
// expect(element).toHaveTextContent(/react/i)
// learn more: https://github.com/testing-library/jest-dom
import '@testing-library/jest-dom';

// Mock axios to avoid actual API calls during tests
jest.mock('axios');

// Silence act() warnings and deprecation warnings
const originalError = console.error;
console.error = (...args) => {
  if (args[0].includes('not wrapped in act(...)')
      || args[0].includes('DEP0040')
      || args[0].includes('punycode')) {
    return;
  }
  originalError.call(console, ...args);
};

// Global test setup
afterEach(() => {
  jest.clearAllTimers();
  jest.clearAllMocks();
});