// script.js
// Conversión de los handlers del PrincipalPage.java
// No se modificó la "lógica" principal: hover cambia color y texto, botones "Siguiente"/"Volver" cambian cards,
// drag mueve la ventana (simulado en DOM), exit oculta la app.

/* ---------------- utilidades ---------------- */
const $ = (sel) => document.querySelector(sel);
const $$ = (sel) => Array.from(document.querySelectorAll(sel));

/* ---------------- elementos ---------------- */
const appWindow = $('#appWindow');
const dragPanel = $('#dragPanel');
const exitBtn = $('#exitBtn');
const minimizeBtn = $('#minimizeBtn');

const cardPanel = $('#cardPanel');
const cards = $$('.card-panel .card');

const btnSiguiente = $('#btnSiguiente');
const btnSiguiente1 = $('#btnSiguiente1');
const btnVolver1 = $('#btnVolver1');

const linealSearchLabel = $('#linealSearchLabel');
const binarySearchLabel = $('#binarySearchLabel');
const hashingSearchlabel = $('#hashingSearchlabel');

const dinamicoTxt = $('#dinamicoTxt');
const dinamicoTxt1 = $('#dinamicoTxt1');

const residualTreeLabel = $('#residualTreeLabel');
const digitalTreeLabel = $('#digitalTreeLabel');
const multipleTreeLabel = $('#multipleTreeLabel');
const huffmanTreeLabel = $('#huffmanTreeLabel');

let currentCardIndex = 0;

/* ---------------- textos (tomados del Java) ---------------- */
const TEXTS = {
  default: "<div class='center-text'><strong></strong><br></div>",

  lineal: "<div class='center-text'><strong>BUSQUEDA LINEAL</strong><br> Algoritmo de búsqueda secuencial que recorre cada elemento hasta encontrar el valor buscado.</div>",
  binary: "<div class='center-text'><strong>BUSQUEDA BINARIA</strong><br> Algoritmo que busca en un array ordenado dividiendo el espacio de búsqueda por la mitad en cada paso.</div>",
  hashing: "<div class='center-text'><strong>FUNCIONES HASH</strong><br> Funciones que mapean datos de tamaño arbitrario a valores de tamaño fijo para búsquedas rápidas.</div>",

  residual: "<div class='center-text'><strong>ARBOL POR RESIDUOS</strong><br> Estructura de árbol basada en residuos (descripción derivada de la etiqueta Java).</div>",
  digital: "<div class='center-text'><strong>ARBOLES DIGITALES</strong><br> Árboles especializados en representaciones digitales (trie, árboles bit a bit).</div>",
  multiple: "<div class='center-text'><strong>RESIDUOS MULTIPLES</strong><br> Variante de árbol que maneja residuos múltiples (etiqueta original del Java).</div>",
  huffman: "<div class='center-text'><strong>ARBOLES HUFFMAN</strong><br> Árboles para codificación Huffman (compresión de datos mediante frecuencias).</div>"
};

/* ---------------- helpers para cards ---------------- */
function showCard(idx){
  currentCardIndex = Math.max(0, Math.min(cards.length-1, idx));
  cards.forEach((c,i)=>{
    c.style.display = (i===currentCardIndex) ? 'flex' : 'none';
  });
}

/* ---------------- eventos de navegación (preservando lógica) ---------------- */
btnSiguiente.addEventListener('click', (evt)=>{
  // btnSiguienteMouseClicked logic -> avanzar CardLayout
  showCard(currentCardIndex + 1);
});

btnSiguiente1.addEventListener('click', (evt)=>{
  // btnSiguiente1MouseClicked (en Java estaba vacío) -> aquí avanzamos también
  showCard(currentCardIndex + 1);
});

btnVolver1.addEventListener('click', (evt)=>{
  // btnVolver1MouseClicked -> retroceder CardLayout
  showCard(currentCardIndex - 1);
});

/* ---------------- hover / click handlers para opciones (preservando nombres) ---------------- */

function setHoverLabel(labelEl, enterText, leaveColor=true){
  // enter
  labelEl.addEventListener('mouseenter', (e)=>{
    labelEl.style.color = 'rgb(124,212,187)'; // new Color(124,212,187)
    if(enterText) dinamicoTxt.innerHTML = TEXTS[enterText] || TEXTS.default;
  });
  // exit
  labelEl.addEventListener('mouseleave', (e)=>{
    labelEl.style.color = ''; // volver al color por defecto (CSS)
    if(leaveColor) dinamicoTxt.innerHTML = TEXTS.default;
  });
  // click (si el Java tenia click, lo dejamos como placeholder)
  labelEl.addEventListener('click', (e)=>{
    // Aquí se podría abrir la implementación real (en Java probablemente abría otra ventana).
    console.log(labelEl.id + ' clicked');
  });
}

/* configurar los elementos de la primera card */
setHoverLabel(linealSearchLabel, 'lineal');
setHoverLabel(binarySearchLabel, 'binary');
setHoverLabel(hashingSearchlabel, 'hashing');

/* segunda card: estos actualizan dinamicoTxt1 */
function setHoverLabelCard2(labelEl, key){
  labelEl.addEventListener('mouseenter', (e)=>{
    labelEl.style.color = 'rgb(124,212,187)';
    dinamicoTxt1.innerHTML = TEXTS[key] || TEXTS.default;
  });
  labelEl.addEventListener('mouseleave', (e)=>{
    labelEl.style.color = '';
    dinamicoTxt1.innerHTML = TEXTS.default;
  });
  labelEl.addEventListener('click', (e)=>{
    console.log(labelEl.id + ' clicked');
  });
}

setHoverLabelCard2(residualTreeLabel, 'residual');
setHoverLabelCard2(digitalTreeLabel, 'digital');
setHoverLabelCard2(multipleTreeLabel, 'multiple');
setHoverLabelCard2(huffmanTreeLabel, 'huffman');

/* ---------------- exit / minimize ---------------- */
exitBtn.addEventListener('click', (e)=>{
  // exitBtnMouseClicked -> cerramos / ocultamos la app
  appWindow.style.display = 'none';
});
exitBtn.addEventListener('mouseenter', ()=> exitBtn.style.background='#ffecec');
exitBtn.addEventListener('mouseleave', ()=> exitBtn.style.background='');

minimizeBtn.addEventListener('click', (e)=>{
  // En navegador "minimizar" reducimos a una barra: toggle
  if(appWindow.style.height !== '48px'){
    appWindow.style.height = '48px';
    // ocultar contenido
    $$('.content').forEach(el=> el.style.display='none');
  } else {
    appWindow.style.height = '720px';
    $$('.content').forEach(el=> el.style.display='flex');
  }
});

/* ---------------- drag (simula dragPanelMousePressed / dragPanelMouseDragged) ---------------- */
let isDragging = false;
let dragOffset = {x:0,y:0};

dragPanel.addEventListener('mousedown', (e)=>{
  isDragging = true;
  // evt.getX() / getY() equivalentes
  dragOffset.x = e.clientX - appWindow.getBoundingClientRect().left;
  dragOffset.y = e.clientY - appWindow.getBoundingClientRect().top;
  dragPanel.style.cursor = 'grabbing';
});

document.addEventListener('mousemove', (e)=>{
  if(!isDragging) return;
  // evt.getXOnScreen / getYOnScreen -> posicion absoluta del mouse
  const left = e.clientX - dragOffset.x;
  const top = e.clientY - dragOffset.y;
  // limitar dentro ventana visible
  appWindow.style.position = 'absolute';
  appWindow.style.left = Math.max(0, left) + 'px';
  appWindow.style.top = Math.max(0, top) + 'px';
});

document.addEventListener('mouseup', (e)=>{
  if(isDragging){
    isDragging = false;
    dragPanel.style.cursor = 'grab';
  }
});

/* ---------------- inicialización ---------------- */
function init(){
  // reproducir comportamiento default del Java
  showCard(0);
  dinamicoTxt.innerHTML = TEXTS.default;
  dinamicoTxt1.innerHTML = TEXTS.default;

  // reproducir setText del Java en los labels (por si el HTML no los contiene)
  $('#linealSearchLabel').textContent = 'Busqueda Lineal';
  $('#binarySearchLabel').textContent = 'Busqueda Binaria';
  $('#hashingSearchlabel').textContent = 'Funciones Hash';
  $('#residualTreeLabel').textContent = 'Arbol por Residuos';
  $('#digitalTreeLabel').textContent = 'Arboles Digitales';
  $('#multipleTreeLabel').textContent = 'Residuos Multiples';
  $('#huffmanTreeLabel').textContent = 'Arboles Huffman';
}

init();
