module.exports = {
  mode: "jit",
  purge: [
    "./src/main/kotlin/**/*.kt",
    "./src/main/resources/templates/**/*.{js,html}",
    "./src/main/resources/static/**/*.{js,html}",
  ],
  darkMode: 'class', // or 'media' or 'class'
  theme: {
    extend: {},
  },
  variants: {
    extend: {},
  },
  plugins: [
    require('daisyui'),
  ],
  daisyui: {
    themes: [
      'light', // first one will be the default theme
      'dark',
    ],
  }
};
