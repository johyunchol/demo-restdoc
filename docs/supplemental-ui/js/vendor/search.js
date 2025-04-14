/* eslint-env browser */
window.search = (function (window) {
  const scriptAttrs = document.getElementById('search-script').dataset
  const basePath = scriptAttrs.basePath
  const pagePath = scriptAttrs.pagePath
  var searchInput = document.getElementById('search-input')
  var searchResult = document.createElement('div')
  searchResult.classList.add('search-result-dropdown-menu')
  searchInput.parentNode.appendChild(searchResult)

  function createSearchResult(result, searchResultDataset) {
    result.forEach(function (item) {
      searchResultDataset.appendChild(createSearchResultItem(item.item))
    })
  }

  function createSearchResultItem (item) {
    var documentTitle = document.createElement('div')
    documentTitle.classList.add('search-result-document-title')
    documentTitle.innerText = item.title
    var documentHitLink = document.createElement('a')
    documentHitLink.href = item.url;
    documentHitLink.appendChild(documentTitle);
    var searchResultItem = document.createElement('div')
    searchResultItem.classList.add('search-result-item')
    searchResultItem.classList.add('hit')
    searchResultItem.appendChild(documentHitLink)
    searchResultItem.addEventListener('mousedown', function (e) {
      e.preventDefault()
    })
    return searchResultItem
  }

  function createNoResult (text) {
    var searchResultItem = document.createElement('div')
    searchResultItem.classList.add('search-result-item')
    var documentHit = document.createElement('div')
    documentHit.innerText = '결과가 없네요 :(';
    searchResultItem.appendChild(documentHit)
    return searchResultItem
  }

  function search (index, text) {
    return index.search(text);
  }

  function searchIndex (index, text) {
    while (searchResult.firstChild) {
      searchResult.removeChild(searchResult.firstChild)
    }
    if (text.trim() === '') {
      return
    }
    var result = search(index, text)
    var searchResultDataset = document.createElement('div')
    searchResultDataset.classList.add('search-result-dataset')
    searchResult.appendChild(searchResultDataset)
    if (result.length > 0) {
      createSearchResult(result, searchResultDataset)
    } else {
      searchResultDataset.appendChild(createNoResult(text))
    }
  }

  function debounce (func, wait, immediate) {
    var timeout
    return function () {
      var context = this
      var args = arguments
      var later = function () {
        timeout = null
        if (!immediate) func.apply(context, args)
      }
      var callNow = immediate && !timeout
      clearTimeout(timeout)
      timeout = setTimeout(later, wait)
      if (callNow) func.apply(context, args)
    }
  }

  function init (index) {
    var search = debounce(function () {
      searchIndex(index, searchInput.value)
    }, 100)
    searchInput.addEventListener('keydown', search)

    // this is prevented in case of mousedown attached to SearchResultItem
    searchInput.addEventListener('blur', function (e) {
      while (searchResult.firstChild) {
        searchResult.removeChild(searchResult.firstChild)
      }
    })
  }

  return {
    init: init,
  }
})(window)
