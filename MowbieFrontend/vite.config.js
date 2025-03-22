import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

import tailwindcss from '@tailwindcss/vite'

import dotenv from 'dotenv';

dotenv.config();

export default defineConfig({
  plugins: [react(), tailwindcss(),],
  server: {
    host: '192.168.10.1',
    port: 5173,
    strictPort: true,
  }
})
