import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  // build 경로 설정
  build: {
    outDir: '../backend/autotrip/src/main/resources/static',
    emptyOutDir: true
  },
  css: {
    postcss: {
      map: false // 빌드 안 되는 문제 해결 위해 postcss map 비활성화
    }
  }
})
