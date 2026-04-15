/* ─── CAR DATA ─── */
/* Exported as `allCars` so frota.html can access the full list.
   index.html shows only the first 6 as a preview. */
const allCars = [
  { name: 'Renault Kwid',        cat: 'economico',     label: 'Econômico',     price: 79,  km: '150km/d',  pass: '5', trans: 'Manual',    badge: '',             badgeClass: '' },
  { name: 'Volkswagen Polo',     cat: 'economico',     label: 'Econômico',     price: 98,  km: '200km/d',  pass: '5', trans: 'Automático', badge: 'Mais alugado', badgeClass: '' },
  { name: 'Fiat Mobi',           cat: 'economico',     label: 'Econômico',     price: 72,  km: '150km/d',  pass: '5', trans: 'Manual',    badge: '',             badgeClass: '' },
  { name: 'Chevrolet Onix',      cat: 'economico',     label: 'Econômico',     price: 89,  km: '180km/d',  pass: '5', trans: 'Automático', badge: '',             badgeClass: '' },
  { name: 'Hyundai HB20',        cat: 'economico',     label: 'Econômico',     price: 85,  km: '150km/d',  pass: '5', trans: 'Manual',    badge: '',             badgeClass: '' },
  { name: 'Chevrolet Onix Plus', cat: 'intermediario', label: 'Intermediário', price: 119, km: '250km/d',  pass: '5', trans: 'Automático', badge: '',             badgeClass: '' },
  { name: 'Toyota Corolla',      cat: 'intermediario', label: 'Intermediário', price: 179, km: 'Ilimitado', pass: '5', trans: 'Automático', badge: 'Top Rated',   badgeClass: 'gold' },
  { name: 'Fiat Pulse',          cat: 'intermediario', label: 'Intermediário', price: 139, km: '200km/d',  pass: '5', trans: 'Automático', badge: 'Novo',         badgeClass: '' },
  { name: 'Volkswagen Virtus',   cat: 'intermediario', label: 'Intermediário', price: 129, km: '220km/d',  pass: '5', trans: 'Automático', badge: '',             badgeClass: '' },
  { name: 'Honda Civic',         cat: 'intermediario', label: 'Intermediário', price: 169, km: 'Ilimitado', pass: '5', trans: 'Automático', badge: '',             badgeClass: '' },
  { name: 'Jeep Compass',        cat: 'suv',           label: 'SUV',           price: 219, km: 'Ilimitado', pass: '5', trans: 'Automático', badge: '',             badgeClass: '' },
  { name: 'Hyundai Creta',       cat: 'suv',           label: 'SUV',           price: 189, km: 'Ilimitado', pass: '5', trans: 'Automático', badge: '',             badgeClass: '' },
  { name: 'Toyota SW4',          cat: 'suv',           label: 'SUV',           price: 289, km: 'Ilimitado', pass: '7', trans: 'Automático', badge: '',             badgeClass: '' },
  { name: 'Jeep Renegade',       cat: 'suv',           label: 'SUV',           price: 199, km: 'Ilimitado', pass: '5', trans: 'Automático', badge: 'Novo',         badgeClass: '' },
  { name: 'BMW 320i',            cat: 'premium',       label: 'Premium',       price: 349, km: 'Ilimitado', pass: '5', trans: 'Automático', badge: 'Premium',      badgeClass: 'gold' },
  { name: 'Mercedes C 200',      cat: 'premium',       label: 'Premium',       price: 389, km: 'Ilimitado', pass: '5', trans: 'Automático', badge: 'Premium',      badgeClass: 'gold' },
  { name: 'Nissan Leaf',         cat: 'eletrico',      label: 'Elétrico',      price: 199, km: '300km/d',  pass: '5', trans: 'Automático', badge: '⚡ Zero CO₂',   badgeClass: '' },
  { name: 'BYD Dolphin',         cat: 'eletrico',      label: 'Elétrico',      price: 219, km: '400km/d',  pass: '5', trans: 'Automático', badge: '⚡ Zero CO₂',   badgeClass: '' },
];

/* index.html preview: first 6 cards */
const cars = allCars.slice(0, 6);

/* ─── SVG CAR ILLUSTRATION ─── */
function drawCarSVG(cat) {
  const col = {
    economico:     '#7B3FD8',
    intermediario: '#5A20A0',
    suv:           '#8B50E0',
    premium:       '#4E1A9E',
    eletrico:      '#9B6AE8',
  };
  const c = col[cat] || '#7B3FD8';
  return `<svg viewBox="0 0 280 120" fill="none" xmlns="http://www.w3.org/2000/svg" style="width:82%;margin:auto;display:block;">
    <ellipse cx="140" cy="107" rx="128" ry="7" fill="rgba(106,38,205,0.1)"/>
    <path d="M30 85 Q35 60 70 48 Q105 36 140 34 Q175 32 205 45 Q235 55 248 72 Q258 82 260 95 L30 95 Z" fill="${c}" opacity="0.9"/>
    <path d="M88 68 Q95 44 130 38 Q162 32 185 45 Q205 55 210 72 Z" fill="${c}" opacity="0.6"/>
    <rect x="90" y="45" width="55" height="24" rx="4" fill="rgba(200,180,255,0.45)"/>
    <rect x="152" y="48" width="48" height="21" rx="4" fill="rgba(200,180,255,0.35)"/>
    <line x1="98" y1="52" x2="103" y2="68" stroke="rgba(255,255,255,0.3)" stroke-width="1.5"/>
    <circle cx="80"  cy="99" r="15" fill="#2A1A4A"/>
    <circle cx="80"  cy="99" r="10" fill="#3E2870"/>
    <circle cx="80"  cy="99" r="5"  fill="${c}"/>
    <circle cx="200" cy="99" r="15" fill="#2A1A4A"/>
    <circle cx="200" cy="99" r="10" fill="#3E2870"/>
    <circle cx="200" cy="99" r="5"  fill="${c}"/>
    <ellipse cx="255" cy="76" rx="7" ry="4" fill="#E0D4FF" opacity="0.9"/>
    <rect x="28" y="76" width="5" height="10" rx="2" fill="${c}" opacity="0.9"/>
  </svg>`;
}

/* ─── RENDER (index.html preview) ─── */
function renderCars(filter) {
  const grid = document.getElementById('carGrid');
  if (!grid) return;
  const list = filter === 'todos' ? cars : cars.filter(c => c.cat === filter);

  grid.innerHTML = list.map((car, i) => `
    <div class="car-card reveal" style="animation-delay:${i * 0.07}s">
      <div class="car-img-wrap">
        ${car.badge ? `<span class="car-badge ${car.badgeClass}">${car.badge}</span>` : ''}
        ${drawCarSVG(car.cat)}
      </div>
      <div class="car-info">
        <div class="car-category">${car.label}</div>
        <div class="car-name">${car.name}</div>
        <div class="car-specs">
          <div class="spec-item">
            <span class="spec-val">${car.km}</span>
            <span class="spec-lbl">Quilômetros</span>
          </div>
          <div class="spec-item">
            <span class="spec-val">${car.pass} lug.</span>
            <span class="spec-lbl">Passageiros</span>
          </div>
          <div class="spec-item">
            <span class="spec-val">${car.trans}</span>
            <span class="spec-lbl">Câmbio</span>
          </div>
        </div>
        <div class="car-footer">
          <div class="car-price">
            <div class="price-from">A partir de</div>
            <div>
              <span class="price-val">R$${car.price}</span>
              <span class="price-unit">/dia</span>
            </div>
          </div>
          <button class="btn-rent">Reservar</button>
        </div>
      </div>
    </div>
  `).join('');

  setTimeout(() => {
    document.querySelectorAll('.car-card.reveal').forEach(el => el.classList.add('visible'));
  }, 80);
}

function filterCat(el, cat) {
  document.querySelectorAll('.cat-card').forEach(c => c.classList.remove('active'));
  el.classList.add('active');
  renderCars(cat);
}

function buscarCarros() {
  const loc = document.getElementById('location').value;
  if (!loc) {
    alert('Por favor, informe o local de retirada.');
    return;
  }
  document.getElementById('fleet-section').scrollIntoView({ behavior: 'smooth' });
}

/* ─── SCROLL REVEAL ─── */
const observer = new IntersectionObserver(entries => {
  entries.forEach(e => {
    if (e.isIntersecting) {
      e.target.classList.add('visible');
      observer.unobserve(e.target);
    }
  });
}, { threshold: 0.08 });

document.querySelectorAll('.reveal').forEach(el => observer.observe(el));

/* ─── INIT (index only) ─── */
if (document.getElementById('carGrid') && document.querySelector('.cat-grid')) {
  renderCars('todos');
  setTimeout(() => {
    document.querySelectorAll('.reveal').forEach(el => observer.observe(el));
  }, 200);
}