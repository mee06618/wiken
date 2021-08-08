module.exports = {
  mode: "jit",
  purge: [
    "./src/main/kotlin/**/*.kt",
    "./src/main/resources/templates/**/*.{js,html}",
    "./src/main/resources/static/**/*.{js,html}",
  ],
  darkMode: false, // or 'media' or 'class'
  theme: {
    extend: {},
  },
  variants: {
    extend: {},
  },
  plugins: [
    require('daisyui'),
  ],
};
