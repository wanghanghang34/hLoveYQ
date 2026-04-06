/* Minimal offline cache for "护你周全" */
const CACHE = "huniuzhouquan-v1";
const ASSETS = [
  "/",
  "/login",
  "/css/app.css",
  "/manifest.webmanifest",
  "/img/icon.svg"
];

self.addEventListener("install", (event) => {
  event.waitUntil(
    caches.open(CACHE).then((cache) => cache.addAll(ASSETS)).then(() => self.skipWaiting())
  );
});

self.addEventListener("activate", (event) => {
  event.waitUntil(
    caches.keys().then((keys) =>
      Promise.all(keys.filter((k) => k !== CACHE).map((k) => caches.delete(k)))
    ).then(() => self.clients.claim())
  );
});

self.addEventListener("fetch", (event) => {
  const req = event.request;
  const url = new URL(req.url);

  // Only handle same-origin GET
  if (req.method !== "GET" || url.origin !== self.location.origin) return;

  event.respondWith(
    caches.match(req).then((cached) => {
      if (cached) return cached;
      return fetch(req).then((resp) => {
        // Cache basic static assets
        if (resp.ok && (url.pathname.startsWith("/css/") || url.pathname.startsWith("/img/"))) {
          const copy = resp.clone();
          caches.open(CACHE).then((cache) => cache.put(req, copy));
        }
        return resp;
      }).catch(() => cached || new Response("离线中：请连接网络后再试。", { status: 503 }));
    })
  );
});

