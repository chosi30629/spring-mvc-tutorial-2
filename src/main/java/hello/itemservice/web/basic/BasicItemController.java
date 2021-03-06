package hello.itemservice.web.basic;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {
	
	private final ItemRepository itemRepository;
	
	@GetMapping
	public String items(Model model) {
		List<Item> items = itemRepository.findAll();
		model.addAttribute("items", items);
		
		return "/basic/items";
	}

	@GetMapping("/{itemId}")
	public String item(@PathVariable long itemId, Model model) {
		Item item = itemRepository.findById(itemId);
		model.addAttribute("item", item);
		return "/basic/item";
	}
	
	@GetMapping("/add")
	public String addForm() {
		return "/basic/addForm";
	}

//	@PostMapping("/add")
	public String addItemV1(@RequestParam String itemName,
						@RequestParam int price,
						@RequestParam Integer quantity,
						Model model) {
		
		Item item = new Item();
		item.setItemName(itemName);
		item.setPrice(price);
		item.setQuantity(quantity);
		
		itemRepository.save(item);
		
		model.addAttribute("item", item);
		
		return "/basic/item";	// model 에 있는 item 을 가지고 상세 페이지 뷰로 이동 됨, 리다이렉트는 아님, 새로고침 시 계속 add post 호출됨
	}

//	@PostMapping("/add")
	public String addItemV2(@ModelAttribute("item") Item item) {	// 모델명 "item"
		itemRepository.save(item);
		
//		model.addAttribute("item", item);	// 자동 추가, 생략 가능
		
		return "/basic/item";
	}

//	@PostMapping("/add")
	public String addItemV3(@ModelAttribute Item item) {	// 생략 시 클래스명의 맨 앞 문자를 소문자로 변경
		itemRepository.save(item);
		return "/basic/item";
	}
	
//	@PostMapping("/add")
	public String addItemV4(Item item) {	// 어노테이션 생략 시 복잡한 객체일 경우  @ModelAttribute 기본 설정
		itemRepository.save(item);
		return "/basic/item";
	}
	
//	@PostMapping("/add")
	public String addItemV5(Item item) {
		itemRepository.save(item);
		return "redirect:/basic/items/" + item.getId();
	}
	
	@PostMapping("/add")
	public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
		Item savedItem = itemRepository.save(item);
		redirectAttributes.addAttribute("itemId", savedItem.getId());
		redirectAttributes.addAttribute("status", true);
		return "redirect:/basic/items/{itemId}"; // {} 치환은 path로 치환, 아닌건 쿼리 파라미터 - http://localhost:8080/basic/items/3?status=true
	}
	
	@GetMapping("/{itemId}/edit")
	public String editForm(@PathVariable Long itemId, Model model) {
		Item item = itemRepository.findById(itemId);
		model.addAttribute("item", item);
		return "/basic/editForm";
	}

	@PostMapping("/{itemId}/edit")
	public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
		itemRepository.update(itemId, item);
		return "redirect:/basic/items/{itemId}";
	}
	
	/**
	 * 테스트용 데이터 추가
	 */
	@PostConstruct
	public void init() {
		itemRepository.save(new Item("itemA", 10000, 10));
		itemRepository.save(new Item("itemB", 20000, 20));
	}
	
}
