package com.karunada.vanya.data

import com.karunada.vanya.R
import com.karunada.vanya.data.model.Animal

object AnimalData {
    fun getAnimals(): List<Animal> = listOf(
        Animal(
            id = 1,
            name = "Black Panther",
            scientificName = "Panthera pardus",
            category = "Mammal",
            shortDescription = "Karnataka's majestic state animal",
            fullDescription = "The Black Panther is one of the most mysterious and awe-inspiring creatures of the Karnataka forests. Found primarily in the dense jungles of Kabini, Nagarhole and Bandipur, this melanistic leopard is a symbol of Karnataka's rich biodiversity.\n\nContrary to popular belief, the Black Panther is not a separate species. It is simply a leopard born with a genetic condition called melanism, which causes excess production of dark pigment. If you look closely in sunlight, you can still see the faint rosette patterns beneath its dark coat.\n\nKabini in Karnataka is one of the best places in the entire world to spot a Black Panther in the wild. The famous Black Panther of Kabini, nicknamed 'Saya', became a global celebrity and attracted wildlife photographers from over 50 countries.",
            funFact = "A Black Panther is actually a leopard with extra melanin! In bright sunlight you can still see the faint spots beneath its dark coat.",
            habitat = "Dense tropical forests of Kabini, Nagarhole and Bandipur in Karnataka",
            diet = "Deer, wild boar, monkeys, birds and smaller mammals",
            lifespan = "12–17 years in the wild",
            conservationStatus = "Vulnerable",
            threat = "Habitat loss, poaching and human-wildlife conflict are the biggest threats to this magnificent animal.",
            imageRes = R.drawable.panther
        ),
        Animal(
            id = 2,
            name = "Indian Elephant",
            scientificName = "Elephas maximus indicus",
            category = "Mammal",
            shortDescription = "The gentle giant of Karnataka's forests",
            fullDescription = "The Indian Elephant is the largest land animal in Asia and Karnataka is home to one of the largest elephant populations in all of India — over 6,000 individuals roam freely across the state's forests.\n\nElephants are highly intelligent animals with complex social structures. They live in matriarchal herds led by the oldest female, called the matriarch. She guides the herd to water sources and food, remembering routes her own mother taught her decades ago.\n\nIn Karnataka, elephants move between Nagarhole, Bandipur, BR Hills and the Nilgiris through ancient corridors. When these corridors are blocked by roads, farms or villages, elephants are forced into human settlements — leading to crop raids and dangerous conflict.\n\nThe Karnataka Forest Department runs several programs to protect elephants, including elephant-proof trenches, early warning systems and compensation for farmers whose crops are damaged.",
            funFact = "Elephants can recognize themselves in a mirror — one of only a handful of animals on Earth with this level of self-awareness!",
            habitat = "Nagarhole, Bandipur, BR Hills and Bhadra forests of Karnataka",
            diet = "Grasses, leaves, bark, fruit and roots — up to 150 kg of food per day!",
            lifespan = "60–70 years in the wild",
            conservationStatus = "Endangered",
            threat = "Shrinking forest corridors, railway lines cutting through forests, and human-elephant conflict are major threats.",
            imageRes = R.drawable.elephant
        ),
        Animal(
            id = 3,
            name = "Bengal Tiger",
            scientificName = "Panthera tigris tigris",
            category = "Mammal",
            shortDescription = "The apex predator of India's jungles",
            fullDescription = "The Bengal Tiger is India's national animal and Karnataka is its greatest stronghold. With over 500 tigers, Karnataka has more tigers than any other state in India — a remarkable conservation success story.\n\nTigers are solitary, territorial animals. A male tiger's territory can span up to 100 square kilometres. They mark their boundaries using scent sprays, scratch marks on trees and roars that can be heard up to 3 kilometres away.\n\nBandipur and Nagarhole national parks in Karnataka are part of the Nilgiri Biosphere Reserve — the largest protected tiger landscape in the world. Project Tiger, launched in 1973, played a huge role in saving tigers from near extinction.\n\nTigers are crucial to the ecosystem as apex predators. By controlling deer and wild boar populations, they indirectly protect forests from overgrazing. A healthy tiger population means a healthy forest for everyone.",
            funFact = "No two tigers have the same stripe pattern — each tiger's stripes are as unique as a human fingerprint!",
            habitat = "Bandipur, Nagarhole, Bhadra and Dandeli-Anshi tiger reserves in Karnataka",
            diet = "Deer, wild boar, gaur (Indian bison) and sometimes elephant calves",
            lifespan = "10–15 years in the wild",
            conservationStatus = "Endangered",
            threat = "Poaching for bones and skin, habitat fragmentation and prey depletion are primary threats.",
            imageRes = R.drawable.tiger
        ),
        Animal(
            id = 4,
            name = "Indian Hornbill",
            scientificName = "Buceros bicornis",
            category = "Bird",
            shortDescription = "The magnificent farmer of the forest",
            fullDescription = "The Great Hornbill is one of the most spectacular birds found in Karnataka's Western Ghats. With its massive bright yellow and black casque (the structure on top of its bill), it is impossible to mistake for any other bird.\n\nHornbills are called 'farmers of the forest' because they swallow fruits whole and disperse seeds across large distances. A single hornbill can disperse seeds of over 80 species of trees, making them absolutely vital to forest regeneration.\n\nThe breeding behaviour of hornbills is extraordinary. When the female is ready to lay eggs, she enters a natural tree hollow and seals herself inside using mud, droppings and fruit pulp — leaving only a narrow slit. The male then feeds her through this slit for the entire incubation period of nearly 40 days. This remarkable behaviour protects the eggs from predators.\n\nIn Karnataka, hornbills are found in the forests of Coorg (Kodagu), Dandeli and the Sharavathi valley.",
            funFact = "Hornbills seal the female inside a tree hole during nesting. The male feeds her through a tiny slit — for nearly 40 days!",
            habitat = "Dense forests of Coorg, Dandeli and Western Ghats in Karnataka",
            diet = "Fruits, insects, small reptiles and rodents",
            lifespan = "35–40 years in the wild",
            conservationStatus = "Vulnerable",
            threat = "Deforestation and loss of large old trees with natural hollows for nesting are the biggest threats.",
            imageRes = R.drawable.hornbill
        ),
        Animal(
            id = 5,
            name = "Sandalwood Tree",
            scientificName = "Santalum album",
            category = "Plant",
            shortDescription = "Karnataka's fragrant state tree",
            fullDescription = "The Indian Sandalwood tree is Karnataka's state tree and one of the most economically and culturally valuable trees in the world. Karnataka, particularly the districts of Mysuru, Chamarajanagar and Hassan, produces some of the finest sandalwood in existence.\n\nSandalwood is a semi-parasitic tree — its roots tap into the roots of nearby trees and plants to absorb water and nutrients. Despite this, it causes no visible harm to its host plants.\n\nThe fragrant heartwood of the sandalwood tree takes decades to develop. Trees must be at least 30 years old before the aromatic heartwood forms. The older the tree, the richer and deeper the fragrance — which is why mature sandalwood trees are so incredibly valuable.\n\nIn Karnataka, sandalwood is used in traditional Mysuru agarbatti (incense sticks), carvings, essential oils and the famous Mysuru Dasara celebrations. The Mysuru palace gates are made of sandalwood.\n\nDue to its extreme value, sandalwood trees in Karnataka are legally owned by the state government, regardless of whose land they grow on.",
            funFact = "Sandalwood keeps its fragrance for decades — even centuries! Ancient artifacts and carvings still smell richly fragrant today.",
            habitat = "Dry deciduous forests of Mysuru, Chamarajanagar and Hassan districts",
            diet = "Semi-parasitic — absorbs nutrients from roots of host plants",
            lifespan = "Can live over 100 years; heartwood develops after 30+ years",
            conservationStatus = "Vulnerable",
            threat = "Illegal poaching of trees, sandalwood spike disease and reduction of host plants threaten this species.",
            imageRes = R.drawable.sandalwood
        ),
        Animal(
            id = 6,
            name = "King Cobra",
            scientificName = "Ophiophagus hannah",
            category = "Reptile",
            shortDescription = "The world's longest venomous snake",
            fullDescription = "The King Cobra is the world's longest venomous snake, capable of reaching lengths of up to 5.5 metres. Despite its fearsome reputation, the King Cobra is actually a shy and reclusive animal that avoids humans whenever possible.\n\nKarnataka's Western Ghats — one of the world's 36 biodiversity hotspots — is ideal habitat for King Cobras. The forests of Coorg (Kodagu), Agumbe and the Sharavathi valley are known to have healthy King Cobra populations.\n\nUnlike most snakes, King Cobras are dedicated parents. The female constructs a nest from leaves and debris, lays her eggs inside, and then guards the nest aggressively until the eggs hatch — about 60–90 days. This is the only snake in the world known to build a true nest.\n\nThe King Cobra's venom is not the most toxic of all snakes, but the sheer volume it can inject in a single bite makes it extremely dangerous. A single bite can deliver enough venom to kill an adult elephant.\n\nAgumbe in Karnataka is famous worldwide for King Cobra research. The Agumbe Rainforest Research Station, founded by herpetologist Romulus Whitaker, has tracked and studied King Cobras here for decades.",
            funFact = "King Cobras mainly eat other snakes — even highly venomous ones like kraits and other cobras! They are immune to many snake venoms.",
            habitat = "Rainforests of Agumbe, Coorg and the Western Ghats in Karnataka",
            diet = "Almost exclusively other snakes, occasionally monitor lizards",
            lifespan = "20–25 years in the wild",
            conservationStatus = "Vulnerable",
            threat = "Habitat loss, persecution by humans out of fear, and collection for the illegal wildlife trade.",
            imageRes = R.drawable.cobra
        )
    )
}