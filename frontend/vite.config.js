import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/test': {
        target: 'http://localhost:2424',
        changeOrigin: true,
        secure: false,
      }
    }
  }
});

