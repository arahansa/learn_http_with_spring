const path = require("path");
const fs = require('fs');
const compress = require('brotli/compress');
var brotli = require('brotli');

const brotliSettings = {
    extension: 'br',
    skipLarger: true,
    mode: 1, // 0 = generic, 1 = text, 2 = font (WOFF2)
    quality: 10, // 0 - 11,
    lgwin: 12 // default
};

fs.readdirSync('./css').forEach(file => {
    if (file.endsWith('.js') || file.endsWith('.css') || file.endsWith('.html')) {
        const buffer = fs.readFileSync(path.resolve(__dirname, './css/' + file))
        const result = brotli.compress(buffer, brotliSettings);
        fs.writeFileSync('./css/' + file + '.br', result);
    }
});